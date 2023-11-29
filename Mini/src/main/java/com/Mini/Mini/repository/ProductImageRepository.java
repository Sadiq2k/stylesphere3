package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage>  findByProduct(Product product);
    void deleteByProductId(Long productId);

    List<ProductImage> findIdsByProduct_Id(ProductImage productId);

    void deleteAllByProductId(Long id);

    void deleteImageByProductId(Long productId);
}
