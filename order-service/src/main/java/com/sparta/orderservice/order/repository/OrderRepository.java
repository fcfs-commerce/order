package com.sparta.orderservice.order.repository;

import com.sparta.orderservice.order.dto.response.OrderSummaryInterface;
import com.sparta.orderservice.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryDSLRepository {

  @Query(value = "SELECT "
      + "    o.order_id AS order_id, "
      + "    MIN(p.name) AS product_name, "
      + "    SUM(oi.quantity) AS quantity, "
      + "    SUM(oi.quantity * oi.price) AS price, "
      + "    o.status AS status, "
      + "    o.created_date_time AS order_date_time "
      + "FROM order_item oi "
      + "JOIN option_items oi2 ON oi.option_item_id = oi2.option_item_id "
      + "JOIN products p ON oi2.product_id = p.product_id "
      + "JOIN orders o ON oi.order_id = o.order_id "
      + "WHERE o.user_id = :userId "
      + "GROUP BY o.order_id "
      + "ORDER BY o.order_id DESC ",
      countQuery = "SELECT COUNT(o.order_id) FROM orders o WHERE o.user_id = :userId",
      nativeQuery = true)
  Page<OrderSummaryInterface> getOrders(Long userId, Pageable pageable);
}
