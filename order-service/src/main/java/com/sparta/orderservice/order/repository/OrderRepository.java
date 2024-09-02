package com.sparta.orderservice.order.repository;

import com.sparta.orderservice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryDSLRepository {

}
