package com.sparta.orderservice.wish.controller;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.wish.dto.request.WishCreateRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateProductOptionRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateQuantityRequestDto;
import com.sparta.orderservice.wish.service.WishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wishes")
@RequiredArgsConstructor
public class WishController {

  private final WishService wishService;

  @PostMapping
  public ResponseEntity<ApiResponse> createWish(@RequestBody @Valid WishCreateRequestDto requestDto,
      @RequestParam("userId") Long userId) {
    ApiResponse apiResponse = wishService.createWish(requestDto, userId);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getWishList(@RequestParam("userId") Long userId,
                                                   @RequestParam(value = "page")int page,
                                                   @RequestParam(value = "size")int size) {

    ApiResponse apiResponse = wishService.getWishList(userId, page-1, size);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{wishId}/quantity")
  public ResponseEntity<ApiResponse> updateWishQuantity(
      @RequestBody WishUpdateQuantityRequestDto requestDto,
      @RequestParam("userId") Long userId,
      @PathVariable Long wishId) {
    ApiResponse apiResponse = wishService.updateWishQuantity(wishId, userId, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{wishId}/option")
  public ResponseEntity<ApiResponse> updateWishProductOption(
      @RequestBody WishUpdateProductOptionRequestDto requestDto,
      @RequestParam("userId") Long userId,
      @PathVariable Long wishId) {
    ApiResponse apiResponse = wishService.updateWishProductOption(wishId, userId, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

}
