package com.sparta.orderservice.order.controller;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import com.sparta.orderservice.order.service.OrderService;
import java.time.LocalDateTime;
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
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

//  TODO : jwt 받아서 사용자 정보 추출 (@RequestHeader)

  @PostMapping
  public ResponseEntity<ApiResponse> createOrder(@RequestParam("userId") Long userId,
      @RequestBody OrderCreateRequestDto requestDto) {
    ApiResponse apiResponse = orderService.createOrder(userId, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getOrders(@PathVariable Long userId,
                                                         @RequestParam("page")int page,
                                                         @RequestParam("size")int size) {
    ApiResponse apiResponse = orderService.getOrders(userId, page-1, size);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId, @RequestParam("userId") Long userId) {
    ApiResponse apiResponse = orderService.getOrder(orderId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long orderId, @RequestParam("userId") Long userId) {
    ApiResponse apiResponse = orderService.cancelOrder(orderId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{orderId}/return")
  public ResponseEntity<ApiResponse> returnProduct(@PathVariable Long orderId, @RequestParam("userId") Long userId) {
    LocalDateTime now = LocalDateTime.now();
    ApiResponse apiResponse = orderService.returnProduct(orderId, userId, now);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

}
