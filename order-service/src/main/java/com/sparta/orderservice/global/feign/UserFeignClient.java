package com.sparta.orderservice.global.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {

  String SIMPLE_CIRCUIT_BREAKER_CONFIG = "simpleCircuitBreakerConfig";

  @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
  @GetMapping("/api/internal/v1/users/{userId}")
  String findUserName(@PathVariable("userId") Long userId);

  private String fallback(Long userId, Throwable throwable) {
    return " ";
  }
}
