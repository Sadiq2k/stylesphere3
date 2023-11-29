package com.Mini.Mini.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class OrderItemDto {

    private BigDecimal price;
    private int quantity;

}
