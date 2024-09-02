package com.sparta.orderservice.order.repository;

import static com.sparta.orderservice.order.entity.QOrder.order;
import static com.sparta.orderservice.order.entity.QOrderItem.orderItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.orderservice.order.entity.OrderItem;
import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemQueryDSLRepositoryImpl implements OrderItemQueryDSLRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<OrderItem> findReturnedOrderItems(LocalDateTime now) {
    LocalDateTime twoDaysAgo = now.minusDays(2);

    return jpaQueryFactory.select(orderItem)
        .from(orderItem)
        .leftJoin(order).on(order.id.eq(orderItem.order.id))
        .where(order.status.eq(OrderStatus.RETURN_REQUESTED).and(order.returnRequestDateTime.between(twoDaysAgo, now)))
        .fetch();
  }
}
