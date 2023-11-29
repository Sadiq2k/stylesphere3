package com.Mini.Mini.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCsvDto {

    private String username;
    private String OrderId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalPrice;
    private String paymentMethod;
    private String productName;
    private Double totalSales;
    private int totalOrders;
}