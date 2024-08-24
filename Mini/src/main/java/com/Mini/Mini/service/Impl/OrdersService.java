package com.Mini.Mini.service.Impl;

import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.User;

import java.util.List;

public interface OrdersService {
    List<Orders> getAllOrderDate();

    List<Orders> getAllOrderPrice();

    Orders getOrderById(Long orderId);

    void saveOrders(Orders order);

    List<Orders> getAllOrders();

    List<Orders> getOrderByUser(User user);

}
