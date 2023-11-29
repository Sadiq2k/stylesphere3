package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class ProductOffer {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_percentage")
    private double discountPercentage;

    @Column(name = "offer_start_date")
    private LocalDate offerStartDate;

    @Column(name = "offer_end_date")
    private LocalDate offerEndDate;

    private double productDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    public boolean isActive() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isEqual(offerStartDate) || (currentDate.isAfter(offerStartDate) && currentDate.isBefore(offerEndDate));
    }

}
