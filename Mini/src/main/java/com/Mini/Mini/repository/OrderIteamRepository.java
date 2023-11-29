package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderIteamRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrder_Id(Long orderId);

    OrderItem findOrderItemsByOrderId(Long order);

    List<OrderItem> findByPrice(int price);


//    List<OrderItem> findByOrders(List<Orders> orders);


//    List<OrderItem> findOrderItemByOrdersById(List<Orders> orders);
}
