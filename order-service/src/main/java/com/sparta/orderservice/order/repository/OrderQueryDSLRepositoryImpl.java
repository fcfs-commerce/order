package com.sparta.orderservice.order.repository;

import static com.sparta.orderservice.order.entity.QOrder.order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.orderservice.order.type.OrderStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class OrderQueryDSLRepositoryImpl implements OrderQueryDSLRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  @Transactional
  public void updateOrderStatusDelivery(LocalDateTime now) {
    LocalDateTime yesterday = now.minusDays(1);
    LocalDateTime twoDaysAgo = now.minusDays(2);
    LocalDateTime threeDaysAgo = now.minusDays(3);

    jpaQueryFactory.update(order)
        .where(order.createdDateTime.between(twoDaysAgo, now).and(order.status.eq(OrderStatus.PREPARING_DELIVERING)))
        .set(order.status, OrderStatus.DELIVERING)
        .execute();

    jpaQueryFactory.update(order)
        .where(order.createdDateTime.between(threeDaysAgo, yesterday).and(order.status.eq(OrderStatus.DELIVERING)))
        .set(order.status, OrderStatus.DELIVERED)
        .execute();

  }

}
