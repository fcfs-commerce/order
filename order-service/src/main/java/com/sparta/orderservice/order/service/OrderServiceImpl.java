package com.sparta.orderservice.order.service;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.global.exception.ExceptionCode;
import com.sparta.orderservice.global.feign.ProductFeignClient;
import com.sparta.orderservice.global.security.service.EncryptService;
import com.sparta.orderservice.global.util.ApiResponseUtil;
import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import com.sparta.orderservice.order.dto.request.OrderItemCreateRequestDto;
import com.sparta.orderservice.order.dto.response.DecryptedDeliveryInfo;
import com.sparta.orderservice.order.dto.response.OptionItemDto;
import com.sparta.orderservice.order.dto.response.OrderInfoDto;
import com.sparta.orderservice.order.dto.response.OrderItemInfoInterface;
import com.sparta.orderservice.order.dto.response.OrderSummaryInterface;
import com.sparta.orderservice.order.entity.Delivery;
import com.sparta.orderservice.order.entity.Order;
import com.sparta.orderservice.order.entity.OrderItem;
import com.sparta.orderservice.global.feign.UserFeignClient;
import com.sparta.orderservice.order.repository.DeliveryRepository;
import com.sparta.orderservice.order.repository.OrderItemRepository;
import com.sparta.orderservice.order.repository.OrderRepository;
import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final DeliveryRepository deliveryRepository;
  private final EncryptService encryptService;

  private final UserFeignClient userFeignClient;
  private final ProductFeignClient productFeignClient;

  private final static int RETURN_PERIOD = 1;

  @Override
  @Transactional
  public ApiResponse createOrder(Long userId, OrderCreateRequestDto requestDto) {

    // 주문 생성
    Order order = Order.from(userId);

    // 주문 아이템 생성
    List<OrderItem> orderItems = new ArrayList<>();
    List<OrderItemCreateRequestDto> orderItemCreateRequestDtoList = requestDto.getOrderItems();
    for (OrderItemCreateRequestDto orderItemCreateRequestDto : orderItemCreateRequestDtoList) {
      // 주문 가능한 상품인지 판별
      OptionItemDto optionItemDto = findOptionItem(orderItemCreateRequestDto.getProductId(),
          orderItemCreateRequestDto.getProductOptionId());
      Long optionItemId = optionItemDto.getOptionItemId();

      // 재고 차감
      int quantity = orderItemCreateRequestDto.getQuantity();
      if (optionItemDto.getStock() - quantity < 0) {
        throw CustomException.from(ExceptionCode.OUT_OF_STOCK);
      }
      updateOptionItemStock(optionItemDto.getOptionItemId(), (-1) * quantity);

      double price = optionItemDto.getPrice();

      OrderItem orderItem = OrderItem.of(order, optionItemId, quantity, price);
      orderItems.add(orderItem);
    }

    // 배송 생성
    Delivery delivery = encodeDelivery(order, requestDto);

    // 주문 저장
    orderRepository.save(order);
    orderItemRepository.saveAll(orderItems);
    deliveryRepository.save(delivery);

    // 배송 복호화
    DecryptedDeliveryInfo decryptedDelivery = decodeDelivery(delivery);

    return ApiResponseUtil.createSuccessResponse("Created the order successfully.", null);
  }

  @Override
  public ApiResponse getOrders(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<OrderSummaryInterface> orders = orderRepository.getOrders(userId, pageable);
    return ApiResponseUtil.createSuccessResponse("Orders loaded successfully.", orders);
  }

  @Override
  public ApiResponse getOrder(Long orderId, Long userId) {
    // 주문 조회
    Order order = findOrder(orderId);

    // 주문 상품 조회
//    List<OrderItem> orderItems = findOrderItemList(orderId);

//   TODO : 주문 상품 추가 정보 조회
    List<OrderItemInfoInterface> orderItems = findOrderItemInfoList(orderId);

    // 배송 정보 조회
    DecryptedDeliveryInfo decryptedDelivery = findDelivery(orderId);

    // 조회 요청자가 주문자인지 판별
    Long orderUserId = order.getUserId();
    hasPermissionForOrder(userId, orderUserId);

//    TODO : 사용자 이름 조회 (user service 통신)
    String encodedUsername = findUserName(userId);
    String userName = encryptService.decrypt(encodedUsername);

    OrderInfoDto orderInfo = OrderInfoDto.of(order, userName, orderItems, decryptedDelivery);
    return ApiResponseUtil.createSuccessResponse("Order loaded successfully.", orderInfo);
  }

  @Override
  @Transactional
  public ApiResponse cancelOrder(Long orderId, Long userId) {
    Order order = findOrder(orderId);

    // 취소 요청이 가능한 사용자인지 판별
    Long orderUserId = order.getUserId();
    hasPermissionForOrder(userId, orderUserId);

    // 주문 상태가 배송 중이 되기 전인지 판별
    OrderStatus orderStatus = order.getStatus();
    if (!(orderStatus.equals(OrderStatus.PREPARING_PRODUCT) || orderStatus.equals(OrderStatus.PREPARING_DELIVERING))) {
      throw CustomException.from(ExceptionCode.ALREADY_SHIPPED);
    }

    // 주문 취소
    order.cancelOrder();

    // 상품 재고 복구
    List<OrderItem> orderItems = findOrderItemList(orderId);
    for (OrderItem orderItem : orderItems) {
      // 재고 복구
      updateOptionItemStock(orderItem.getOptionItemId(), orderItem.getQuantity());
    }

    return ApiResponseUtil.createSuccessResponse("Canceled the order successfully.", null);
  }

  @Override
  @Transactional
  public ApiResponse returnProduct(Long orderId, Long userId, LocalDateTime now) {
    Order order = findOrder(orderId);

    // 반품 요청이 가능한 사용자인지 판별
    Long orderUserId = order.getUserId();
    hasPermissionForOrder(userId, orderUserId);

    // 주문 상태가 배송 완료인지 판별
    OrderStatus orderStatus = order.getStatus();
    if (!orderStatus.equals(OrderStatus.DELIVERED)) {
      throw CustomException.from(ExceptionCode.RETURN_NOT_ALLOWED);
    }

    // 반품 요청 가능 기간인지 판별
    Delivery delivery = deliveryRepository.findByOrderId(orderId);
    LocalDate completedDate = delivery.getCompletedDateTime().toLocalDate();
    LocalDate today = now.toLocalDate();
    if (today.isAfter(completedDate.plusDays(RETURN_PERIOD + 1))) {
      throw CustomException.from(ExceptionCode.RETURN_PERIOD_EXPIRED);
    }

    // 반품 신청
    order.requestReturn(now);

    return ApiResponseUtil.createSuccessResponse("Returned the order successfully.", null);
  }

  @Override
  public void updateOrderStatusDelivery(LocalDateTime now) {
    orderRepository.updateOrderStatusDelivery(now);
  }

  @Override
  @Transactional
  public void updateOrderStatusReturn(LocalDateTime now) {
    List<OrderItem> returnedOrderItems = orderItemRepository.findReturnedOrderItems(now);
    for (OrderItem orderItem : returnedOrderItems) {
      // 재고 복구
      updateOptionItemStock(orderItem.getOptionItemId(), orderItem.getQuantity());

      // 반품 완료 상태 전환
      Order order = orderItem.getOrder();
      order.completeReturn();
    }

  }

  private void hasPermissionForOrder(Long userId, Long orderUserId) {
    if(!userId.equals(orderUserId)) {
      throw CustomException.from(ExceptionCode.USER_MISMATCH);
    }
  }

  private DecryptedDeliveryInfo findDelivery(Long orderId) {
    Delivery delivery = deliveryRepository.findByOrderId(orderId);
    return decodeDelivery(delivery);
  }

  private Order findOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> CustomException.from(ExceptionCode.ORDER_NOT_FOUND));
  }

private List<OrderItem> findOrderItemList(Long orderId) {
    return orderItemRepository.findAllByOrderId(orderId);
}

  private List<OrderItemInfoInterface> findOrderItemInfoList(Long orderId) {
    return orderItemRepository.findOrderItems(orderId);
  }

  private DecryptedDeliveryInfo decodeDelivery(Delivery delivery) {
    String name = encryptService.decrypt(delivery.getName());
    String phoneNumber = encryptService.decrypt(delivery.getPhoneNumber());
    String zipCode = encryptService.decrypt(delivery.getZipCode());
    String address = encryptService.decrypt(delivery.getAddress());
    String message = encryptService.decrypt(delivery.getMessage());
    return DecryptedDeliveryInfo.of(delivery.getId(), name, phoneNumber, zipCode, address, message);
  }

  private Delivery encodeDelivery(Order order, OrderCreateRequestDto requestDto) {
    String name = encryptService.encrypt(requestDto.getName());
    String phoneNumber = encryptService.encrypt(requestDto.getPhoneNumber());
    String zipCode = encryptService.encrypt(requestDto.getZipCode());
    String address = encryptService.encrypt(requestDto.getAddress());
    String message = encryptService.encrypt(requestDto.getMessage());
    return Delivery.of(order, name, phoneNumber, zipCode, address, message);
  }

  private void updateOptionItemStock(Long optionItemId, int quantity) {
    productFeignClient.updateOptionItemStock(optionItemId, quantity);
  }

  private OptionItemDto findOptionItem(Long productId, Long productOptionId) {
    OptionItemDto optionItem = null;
    if (productOptionId == null) {
      optionItem = productFeignClient.findOptionItemIdByProductId(productId);
    } else {
      optionItem = productFeignClient.findOptionItemIdByProductIdAndProductOptionId(productId, productOptionId);
    }

    if (optionItem == null) {
      CustomException.from(ExceptionCode.OPTION_ITEM_NOT_FOUND);
    }
    return optionItem;
  }

  private String findUserName(Long userId) {
    return userFeignClient.findUserName(userId);
  }
}
