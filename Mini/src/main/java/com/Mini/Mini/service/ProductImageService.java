package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductImage;
import com.Mini.Mini.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    ProductImageRepository productImageRepository;

    public List<ProductImage> getProductImage(Product product){
        return productImageRepository.findByProduct(product);
    }

    public void removeImageById(Long id){
        productImageRepository.deleteImageByProductId(id);
    }

    public void removeImagesByProductId(Long id) {
        productImageRepository.deleteImageByProductId(id);
    }
}
