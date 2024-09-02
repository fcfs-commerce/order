package com.sparta.orderservice.global.util;

import com.sparta.orderservice.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;

public class ApiResponseUtil {

  public static ApiResponse createSuccessResponse(String message, Object data) {
    return new ApiResponse("success", HttpStatus.OK, message, data);
  }

  public static ApiResponse createExceptionResponse(HttpStatus status, String message) {
    return new ApiResponse("fail", status, message, null);
  }

}
