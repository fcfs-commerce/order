package com.sparta.orderservice.order.dto.request;

import lombok.Getter;

@Getter
public class OrderItemCreateRequestDto {

  private Long optionItemId;
  private int quantity;

}
