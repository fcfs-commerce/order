package com.sparta.orderservice.order.service;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import java.time.LocalDateTime;

public interface OrderService {

  ApiResponse createOrder(Long userId, OrderCreateRequestDto requestDto);

  ApiResponse getOrders(Long userId, int page, int size);

  ApiResponse getOrder(Long orderId, Long userId);

  ApiResponse cancelOrder(Long orderId, Long userId);

  ApiResponse returnProduct(Long orderId, Long userId, LocalDateTime now);

  void updateOrderStatusDelivery(LocalDateTime now);

  void updateOrderStatusReturn(LocalDateTime now);
}
