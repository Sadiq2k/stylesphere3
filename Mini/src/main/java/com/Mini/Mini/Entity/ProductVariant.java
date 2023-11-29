package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductVariant_id",nullable = false)
    private Long id;

    private double price;

    @ManyToOne
    @JoinColumn(name = "variants_id", referencedColumnName = "id")
    private Variants size;



    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
