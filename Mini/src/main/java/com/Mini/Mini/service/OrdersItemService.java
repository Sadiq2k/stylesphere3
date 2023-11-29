package com.Mini.Mini.service;

import com.Mini.Mini.Entity.OrderItem;
import com.Mini.Mini.repository.OrderIteamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersItemService {
    @Autowired
    OrderIteamRepository orderIteamRepository;

    public void saveOrdersItem(OrderItem orderItem){
        orderIteamRepository.save(orderItem);

    }

}
