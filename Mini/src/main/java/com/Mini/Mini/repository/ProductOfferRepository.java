package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductOfferRepository extends JpaRepository<ProductOffer,Long> {

    ProductOffer findProductOfferByProduct(Product product);
}
