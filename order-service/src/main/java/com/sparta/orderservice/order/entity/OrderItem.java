package com.sparta.orderservice.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "option_item_id")
  private Long optionItemId;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Column(name = "price", nullable = false)
  private double price;

  @Builder
  public OrderItem(Order order, Long optionItemId, int quantity, double price) {
    this.order = order;
    this.optionItemId = optionItemId;
    this.quantity = quantity;
    this.price = price;
  }

  public static OrderItem of(Order order, Long optionItemId, int quantity, double price) {
    return OrderItem.builder()
        .order(order)
        .optionItemId(optionItemId)
        .quantity(quantity)
        .price(price)
        .build();
  }
}
