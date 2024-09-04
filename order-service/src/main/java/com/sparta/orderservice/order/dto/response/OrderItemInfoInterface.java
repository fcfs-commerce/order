package com.sparta.orderservice.order.dto.response;

public interface OrderItemInfoInterface {

  Long getOrderItemId();
  Long getProductId();
  String getName();
  String getThumbnailImage();
  int getQuantity();
  double getPrice();

}
