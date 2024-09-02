package com.sparta.orderservice.order.repository;

import com.sparta.orderservice.order.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemQueryDSLRepository {

  List<OrderItem> findReturnedOrderItems(LocalDateTime now);

}
