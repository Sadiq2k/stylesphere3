package com.Mini.Mini.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ProductOfferDto {
private Long productId;
    private Long id;
    private double discountPercentage;
    private LocalDate offerStartDate;
    private LocalDate offerEndDate;
    private double productDiscountAmount;
}
