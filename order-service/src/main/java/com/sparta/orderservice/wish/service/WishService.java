package com.sparta.orderservice.wish.service;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.wish.dto.request.WishCreateRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateProductOptionRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateQuantityRequestDto;

public interface WishService {

  ApiResponse createWish(WishCreateRequestDto requestDto, Long username);

  ApiResponse getWishList(Long userId, int page, int size);

  ApiResponse updateWishQuantity(Long wishId, Long userId, WishUpdateQuantityRequestDto quantityChange);

  ApiResponse updateWishProductOption(Long wishId, Long userId, WishUpdateProductOptionRequestDto requestDto);
}
