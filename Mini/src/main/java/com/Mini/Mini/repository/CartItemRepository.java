package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Cart;
import com.Mini.Mini.Entity.CartItem;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findCartItemByProductAndCart(Product product, Cart cart);

}
