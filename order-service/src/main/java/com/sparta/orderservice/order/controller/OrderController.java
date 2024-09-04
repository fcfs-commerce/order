package com.sparta.orderservice.order.controller;

import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.global.util.JwtUtil;
import com.sparta.orderservice.order.dto.request.OrderCreateRequestDto;
import com.sparta.orderservice.order.service.OrderService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
  public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderCreateRequestDto requestDto,
      HttpServletRequest request) {
    Long userId = getUserId(request);
    ApiResponse apiResponse = orderService.createOrder(userId, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getOrders(HttpServletRequest request,
                                                         @RequestParam("page")int page,
                                                         @RequestParam("size")int size) {
    Long userId = getUserId(request);
    ApiResponse apiResponse = orderService.getOrders(userId, page-1, size);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId, HttpServletRequest request) {
    Long userId = getUserId(request);
    ApiResponse apiResponse = orderService.getOrder(orderId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long orderId, HttpServletRequest request) {
    Long userId = getUserId(request);
    ApiResponse apiResponse = orderService.cancelOrder(orderId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  @PutMapping("/{orderId}/return")
  public ResponseEntity<ApiResponse> returnProduct(@PathVariable Long orderId, HttpServletRequest request) {
    Long userId = getUserId(request);
    LocalDateTime now = LocalDateTime.now();
    ApiResponse apiResponse = orderService.returnProduct(orderId, userId, now);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }

  private Long getUserId(HttpServletRequest request) {
    String token = JwtUtil.getJwtFromHeader(request);
    Claims userInfo = JwtUtil.getUserInfoFromToken(token);
    return Long.parseLong(userInfo.getSubject());
  }
}
