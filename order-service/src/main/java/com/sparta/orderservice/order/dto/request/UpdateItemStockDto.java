package com.sparta.orderservice.order.dto.request;

import com.sparta.orderservice.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateItemStockDto {

  private Long optionItemId;
  private int quantity;

  public static UpdateItemStockDto from(OrderItem orderItem) {
    return UpdateItemStockDto.builder()
        .optionItemId(orderItem.getOptionItemId())
        .quantity(orderItem.getQuantity())
        .build();
  }
}
