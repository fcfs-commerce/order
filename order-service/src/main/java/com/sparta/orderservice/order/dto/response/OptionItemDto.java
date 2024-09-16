package com.sparta.orderservice.order.dto.response;

import lombok.Getter;

@Getter
public class OptionItemDto {

  private Long optionItemId;
  private int quantity;
  private int price;

}
