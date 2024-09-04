package com.sparta.orderservice.order.dto.response;

import com.sparta.orderservice.order.entity.Order;
import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderInfoDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDateTime;
  private OrderStatus status;
  private List<OrderItemInfoInterface> orderItems;
  private DeliveryInfoDto delivery;

  public static OrderInfoDto of(Order order, String userName, List<OrderItemInfoInterface> orderItems,
      DecryptedDeliveryInfo decryptedDelivery) {

    DeliveryInfoDto deliveryInfoDto = DeliveryInfoDto.from(decryptedDelivery);

    return OrderInfoDto.builder()
        .orderId(order.getId())
        .name(userName)
        .orderDateTime(order.getCreatedDateTime())
        .status(order.getStatus())
        .orderItems(orderItems)
        .delivery(deliveryInfoDto)
        .build();
  }
}
