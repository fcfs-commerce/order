package com.sparta.orderservice.global.feign;

import com.sparta.orderservice.order.dto.request.OrderItemCreateRequestDto;
import com.sparta.orderservice.order.dto.request.UpdateItemStockDto;
import com.sparta.orderservice.order.dto.response.OptionItemDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

  String SIMPLE_CIRCUIT_BREAKER_CONFIG = "simpleCircuitBreakerConfig";

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @GetMapping("/api/internal/v1/products/optionItems/{optionItemId}")
  OptionItemDto findOptionItemById(@PathVariable Long optionItemId);

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @PutMapping("/api/internal/v1/products/optionItems/stock-decrease")
  List<OptionItemDto> decreaseStock(@RequestBody List<OrderItemCreateRequestDto> orderItems);

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @PutMapping("/api/internal/v1/products/optionItems/stock-increase")
  void increaseStock(@RequestBody List<UpdateItemStockDto> returnedOrderItemsIdList);

  default OptionItemDto fallback(Long optionItemId, Throwable throwable) {
    return new OptionItemDto();
  }

  default List<OptionItemDto> fallback(List<OrderItemCreateRequestDto> orderItems, Throwable throwable) {
    return new ArrayList<>();
  }

}
