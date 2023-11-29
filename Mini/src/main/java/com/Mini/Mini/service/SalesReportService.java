package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    public Double calculateTotalSales(List<Orders> orders) {

        double totalPrice = 0;
        for (Orders order : orders){
            totalPrice += order.getTotalAmount();
        }
        return totalPrice;
    }
}
