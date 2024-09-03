package com.sparta.orderservice.global.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {

  @GetMapping("/api/v1/users/external/{userId}")
  String findUserName(@PathVariable("userId") Long userId);
}
