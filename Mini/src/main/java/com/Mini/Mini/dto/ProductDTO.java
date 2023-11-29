package com.Mini.Mini.dto;

import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.Entity.Variants;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDTO {

    private int categoryId;
    private Long id;
    private String name;
    private String imageName;
    private String Description;
    private double Price;
    private LocalDateTime dateTime;
    private Long UnitsInStock;

    private List<ProductVariant> selectedVariants;


    public void updateSelectedVariants(List<ProductVariant> variants) {
        this.selectedVariants = variants;
    }
}