package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUserContaining(String searchQuery);

   List<Orders> findOrderByUser(User user);

    List<Orders> findOrderIdByUser(User user);
}
