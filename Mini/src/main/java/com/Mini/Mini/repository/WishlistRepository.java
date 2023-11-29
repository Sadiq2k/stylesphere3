package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findIdByUserId(Long id);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

//    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);
}
