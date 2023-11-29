package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.Entity.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantsRepository extends JpaRepository<ProductVariant,Long> {


    Optional<ProductVariant> findById(Product variantId);

    Optional<ProductVariant> findProductVariantById(Product variantId);

    ProductVariant findSizeById(Long productVariantId);

//    ProductVariant findVariantsId(Long aLong);


//    ProductVariant findVariantsId(Long selectedVariant);
}
