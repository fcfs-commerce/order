package com.sparta.orderservice.global.feign;

import com.sparta.orderservice.order.dto.response.OptionItemDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

  String SIMPLE_CIRCUIT_BREAKER_CONFIG = "simpleCircuitBreakerConfig";

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @GetMapping("/api/internal/v1/products/{productId}/option/{productOptionId}")
  OptionItemDto findOptionItemIdByProductIdAndProductOptionId
      (@PathVariable Long productId, @PathVariable (required = false) Long productOptionId);

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @GetMapping("/api/internal/v1/products/{productId}")
  OptionItemDto findOptionItemIdByProductId(@PathVariable Long productId);

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @PutMapping("/api/internal/v1/products/optionItems/{optionItemId}")
  void updateOptionItemStock(@PathVariable Long optionItemId, @RequestParam int stock);

  default OptionItemDto fallback(Long productId, Long productOptionId, Throwable throwable) {
    return new OptionItemDto();
  }

  default OptionItemDto fallback(Long productId, Throwable throwable) {
    return new OptionItemDto();
  }

  default void fallback(Long optionItemId, int stock) {
    // TODO : 이후 일정 시간에 반영
  }

}
