package com.sparta.orderservice.order.dto.response;

import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDateTime;

public interface OrderSummaryInterface {

  Long getOrderId();
  String getProductName();
  int getQuantity();
  int getPrice();
  OrderStatus getStatus();
  LocalDateTime getOrderDateTime();

}
