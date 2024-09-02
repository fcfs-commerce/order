package com.sparta.orderservice.order.dto.response;

import com.sparta.orderservice.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderItemInfoDto {

  private Long orderItemId;
  private Long productId;
  private String name;
  private String thumbnailImage;
  private int quantity;
  private double price;

  public static OrderItemInfoDto from(OrderItem orderItem) {
//    TODO : 주문 상품 추가 정보 입력
    return OrderItemInfoDto.builder()
        .orderItemId(orderItem.getId())
        .productId(null)
        .name(null)
        .thumbnailImage(null)
        .quantity(orderItem.getQuantity())
        .price(orderItem.getPrice())
        .build();
  }
}
