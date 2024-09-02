package com.sparta.orderservice.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse {

  private String code;
  private HttpStatus status;
  private String message;
  private Object data;

}
