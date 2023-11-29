package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.repository.ProductVariantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductVariantService {
    @Autowired
     ProductVariantsRepository productVariantsRepository;



    public void saveProductVariant(ProductVariant productVariant) {
        productVariantsRepository.save(productVariant);
    }

    public Optional<ProductVariant> getProductVariantById(Product variantId) {
        return productVariantsRepository.findProductVariantById(variantId);
    }

    public ProductVariant getVariantsById(Long variantId) {
        return productVariantsRepository.findById(variantId).orElse(null);
    }
    public ProductVariant findSizeByProductVariantId(Long productVariantId) {
        return productVariantsRepository.findSizeById(productVariantId);
    }

    public Optional<ProductVariant> getVariantsIdByProductVariantId(Long productVariantId) {
        return productVariantsRepository.findById(productVariantId);
    }

    public Optional<ProductVariant> getVariantIdByProductId(Product product) {
        return  productVariantsRepository.findById(product);
    }

    public Object getVariantsByProductId(Long id) {
        return productVariantsRepository.findById(id);
    }


}
