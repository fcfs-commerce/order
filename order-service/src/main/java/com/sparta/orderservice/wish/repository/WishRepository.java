package com.sparta.orderservice.wish.repository;

import com.sparta.orderservice.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

  boolean existsByUserIdAndOptionItemId(Long userId, Long optionItemId);

}
