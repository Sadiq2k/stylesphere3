package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.OrderItem;
import com.Mini.Mini.Entity.Product;

import com.Mini.Mini.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategory_Id(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String keyword);

}
