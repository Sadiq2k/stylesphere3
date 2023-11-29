package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Cart;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

}
