package com.sparta.orderservice.order.dto.response;

import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderSummaryDto {

  private Long orderId;
  private String productName;
  private int quantity;
  private int price;
  private OrderStatus status;
  private LocalDateTime orderDateTime;

}
