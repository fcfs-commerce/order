package com.sparta.orderservice.global.util;

import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.global.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;

public class ParseRequestUtil {

  public static Long extractUserIdFromHeader(HttpServletRequest request) {
    String userIdHeader = request.getHeader("x-claim-userid");
    if (userIdHeader == null) {
      throw CustomException.from(ExceptionCode.MISSING_USER_ID);
    }

    Long userId = Long.parseLong(userIdHeader);
    return userId;
  }

}
