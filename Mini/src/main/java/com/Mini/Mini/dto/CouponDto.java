package com.Mini.Mini.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponDto {

    private Long id;
    private String couponCode;
    private Double discountAmount;
    private Double conditionAmount;
    private String expirationDate;
}
