package com.sparta.orderservice.wish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wishes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wish_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "option_item_id")
  private Long optionItemId;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Builder
  public Wish(Long userId, Long optionItemId, int quantity) {
    this.userId = userId;
    this.optionItemId = optionItemId;
    this.quantity = quantity;
  }

  public static Wish of(Long userId, Long optionItemId, int quantity) {
    return Wish.builder()
        .userId(userId)
        .optionItemId(optionItemId)
        .quantity(quantity)
        .build();
  }

  public void updateQuantity(int quantityChange) {
    this.quantity = this.quantity + quantityChange;
    if (quantity < 1) { // 최소 수량 1
      this.quantity = 1;
    }
  }

  public void updateOptionItem(Long optionItemId) {
    this.optionItemId = optionItemId;
  }
}
