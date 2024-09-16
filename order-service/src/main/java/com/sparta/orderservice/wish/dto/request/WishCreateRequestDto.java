package com.sparta.orderservice.wish.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WishCreateRequestDto {

  @NotNull(message = "옵션 상품 아이디는 필수 입력 사항입니다.")
  private Long optionItemId;

  @NotNull(message = "수량은 필수 입력 사항입니다.")
  private int quantity;

}
