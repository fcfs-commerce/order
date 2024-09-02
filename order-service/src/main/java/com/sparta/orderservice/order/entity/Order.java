package com.sparta.orderservice.order.entity;

import com.sparta.orderservice.global.entity.Timestamped;
import com.sparta.orderservice.order.type.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "return_request_date_time")
  private LocalDateTime returnRequestDateTime;

  @Builder
  public Order(Long userId, OrderStatus status) {
    this.userId = userId;
    this.status = status;
  }

  public static Order from(Long userId) {
    return Order.builder()
        .userId(userId)
        .status(OrderStatus.PREPARING_PRODUCT)
        .build();
  }

  public void cancelOrder() {
    this.status = OrderStatus.ORDER_CANCELED;
  }

  public void requestReturn(LocalDateTime now) {
    this.status = OrderStatus.RETURN_REQUESTED;
    this.returnRequestDateTime = now;
  }

  public void completeReturn() {
    this.status = OrderStatus.RETURN_COMPLETED;
  }
}
