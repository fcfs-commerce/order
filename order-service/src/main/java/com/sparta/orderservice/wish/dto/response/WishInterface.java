package com.sparta.orderservice.wish.dto.response;

public interface WishInterface {

  Long getWishId();
  Long getProductId();
  String getName();
  String getThumbnailImage();
  int getPrice();
  int getQuantity();
  String getProductOptionName();

}
