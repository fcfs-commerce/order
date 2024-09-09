package com.sparta.orderservice.wish.service;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.global.exception.ExceptionCode;
import com.sparta.orderservice.global.feign.ProductFeignClient;
import com.sparta.orderservice.global.util.ApiResponseUtil;
import com.sparta.orderservice.order.dto.response.OptionItemDto;
import com.sparta.orderservice.wish.dto.request.WishCreateRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateProductOptionRequestDto;
import com.sparta.orderservice.wish.dto.request.WishUpdateQuantityRequestDto;
import com.sparta.orderservice.wish.dto.response.WishInterface;
import com.sparta.orderservice.wish.entity.Wish;
import com.sparta.orderservice.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

  private final WishRepository wishRepository;

  private final ProductFeignClient productFeignClient;

  @Override
  public ApiResponse createWish(WishCreateRequestDto requestDto, Long userId) {
    // 존재하는 옵션 상품인지 판별
    Long optionItemId = findOptionItemId(requestDto.getProductId(), requestDto.getProductOptionId());

    // 이미 위시리스트에 동일한 옵션 상품을 담았는지 판별
    boolean isExist = isExistsByWishByUserAndOrderItem(userId, optionItemId);
    if (isExist) {
      throw CustomException.from(ExceptionCode.WISH_EXISTS);
    }

    // 위시 저장
    Wish wish = Wish.of(userId, optionItemId, requestDto.getQuantity());
    wishRepository.save(wish);

    return ApiResponseUtil.createSuccessResponse("Created the wish successfully.", null);
  }

  @Override
  public ApiResponse getWishList(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<WishInterface> wishList = findWishList(userId, pageable);
    return ApiResponseUtil.createSuccessResponse("Wishlist loaded successfully.", wishList);
  }

  @Override
  @Transactional
  public ApiResponse updateWishQuantity(Long wishId, Long userId,
      WishUpdateQuantityRequestDto requestDto) {
    Wish wish = findWishById(wishId);

    // 위시 생성자와 사용자가 동일한지 판별
    hasPermissionForWishUpdate(userId, wish.getUserId());

    // quantityChange 값 만큼 수량 변경
    wish.updateQuantity(requestDto.getQuantityChange());

    return ApiResponseUtil.createSuccessResponse("Updated the wish quantity successfully.", null);
  }

  @Override
  @Transactional
  public ApiResponse updateWishProductOption(Long wishId, Long userId,
      WishUpdateProductOptionRequestDto requestDto) {
    Wish wish = findWishById(wishId);

    // 위시 생성자와 사용자가 동일한지 판별
    hasPermissionForWishUpdate(userId, wish.getUserId());

    // 존재하는 옵션 상품인지 판별
    Long optionItemId = findOptionItemId(requestDto.getProductId(), requestDto.getProductOptionId());

    // 상품 옵션 변경
    wish.updateOptionItem(optionItemId);

    return ApiResponseUtil.createSuccessResponse("Updated the wish product option successfully.", null);
  }

  private void hasPermissionForWishUpdate(Long userId, Long wishUserId) {
    if(!userId.equals(wishUserId)) {
      throw CustomException.from(ExceptionCode.USER_MISMATCH);
    }
  }

  private Wish findWishById(Long wishId) {
    return wishRepository.findById(wishId)
        .orElseThrow(() -> CustomException.from(ExceptionCode.WISH_NOT_FOUND));
  }

  private Page<WishInterface> findWishList(Long userId, Pageable pageable) {
    return wishRepository.findWishList(userId, pageable);
  }

  private Long findOptionItemId(Long productId, Long productOptionId) {
    OptionItemDto optionItem;
    if (productOptionId == null) {
      optionItem = productFeignClient.findOptionItemIdByProductId(productId);
    } else {
      optionItem = productFeignClient.findOptionItemIdByProductIdAndProductOptionId(productId, productOptionId);
    }

    if (optionItem.getOptionItemId() == null) {
      throw CustomException.from(ExceptionCode.PRODUCT_SERVICE_UNAVAILABLE);
    }
    return optionItem.getOptionItemId();
  }

  private boolean isExistsByWishByUserAndOrderItem(Long userId, Long optionItemId) {
    return wishRepository.existsByUserIdAndOptionItemId(userId, optionItemId);
  }

}
