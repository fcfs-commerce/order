package com.sparta.orderservice.order.repository;

import com.sparta.orderservice.order.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

  Delivery findByOrderId(Long orderId);

}
