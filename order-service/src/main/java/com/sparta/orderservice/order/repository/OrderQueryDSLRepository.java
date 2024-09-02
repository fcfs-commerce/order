package com.sparta.orderservice.order.repository;

import java.time.LocalDateTime;

public interface OrderQueryDSLRepository {

  void updateOrderStatusDelivery(LocalDateTime now);

}
