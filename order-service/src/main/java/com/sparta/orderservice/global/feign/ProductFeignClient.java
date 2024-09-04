package com.sparta.orderservice.global.feign;

import com.sparta.orderservice.order.dto.response.OptionItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

  @GetMapping("/api/internal/v1/products/{productId}/option/{productOptionId}")
  OptionItemDto findOptionItemIdByProductIdAndProductOptionId
      (@PathVariable Long productId, @PathVariable (required = false) Long productOptionId);

  @GetMapping("/api/internal/v1/products/{productId}")
  OptionItemDto findOptionItemIdByProductId(@PathVariable Long productId);

  @PutMapping("/api/internal/v1/products/optionItems/{optionItemId}")
  void updateOptionItemStock(@PathVariable Long optionItemId, @RequestParam int stock);

}
