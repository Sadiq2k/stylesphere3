package com.Mini.Mini.dto;

import com.Mini.Mini.Entity.CartItem;
import com.Mini.Mini.Enam.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {


    private List<CartItem> item;

    private LocalDateTime orderDate;

    private int totalAmount;

    private OrderStatus status;

}
