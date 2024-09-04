package com.sparta.orderservice.order.repository;

import com.sparta.orderservice.order.dto.response.OrderItemInfoInterface;
import com.sparta.orderservice.order.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemQueryDSLRepository {

  List<OrderItem> findAllByOrderId(Long orderId);

  @Query(value = "SELECT ori.order_item_id, p.product_id, p.name, p.thumbnail_image, ori.quantity, ori.price "
      + "FROM orders o "
      + "JOIN order_item ori on o.order_id = ori.order_id "
      + "JOIN option_items opi on ori.option_item_id = opi.option_item_id "
      + "JOIN products p on opi.product_id = p.product_id "
      + "WHERE o.order_id = :orderId;",
      nativeQuery = true)
  List<OrderItemInfoInterface> findOrderItems(Long orderId);
}
