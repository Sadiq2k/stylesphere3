package com.Mini.Mini.service;
import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    OrderRepository orderRepository;


    public void saveOrders(Orders orders) {orderRepository.save(orders);}

    public List<Orders> getAllOrders() {
       return orderRepository.findAll();
    }

    public Orders getOrderById(Long orderId) {
        Optional<Orders> orderOptional = orderRepository.findById(orderId);
        return orderOptional.orElse(null);
    }
    public List<Orders> getOrderByUser(User user){
        return orderRepository.findOrderByUser(user);
    }

    public List<Orders> getAllOrderDate() {
        return orderRepository.findAll();
    }

    public List<Orders> getAllOrderPrice() {
        return orderRepository.findAll();
    }
}
