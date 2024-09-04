package com.sparta.orderservice.wish.repository;

import com.sparta.orderservice.wish.dto.response.WishInterface;
import com.sparta.orderservice.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishRepository extends JpaRepository<Wish, Long> {

  boolean existsByUserIdAndOptionItemId(Long userId, Long optionItemId);

  @Query(value = "SELECT w.wish_id, p.product_id, p.name , p.thumbnail_image, p.price, w.quantity, po.name as product_option_name "
      + "FROM wishes w "
      + "JOIN option_items opi ON w.option_item_id = opi.option_item_id "
      + "JOIN products p ON opi.product_id = p.product_id "
      + "LEFT JOIN product_options po ON opi.product_option_id = po.product_option_id "
      + "WHERE w.user_id = :userId",
      nativeQuery = true)
  Page<WishInterface> findWishList(Long userId, Pageable pageable);
}
