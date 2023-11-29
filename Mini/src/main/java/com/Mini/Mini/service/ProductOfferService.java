package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductOffer;
import com.Mini.Mini.repository.ProductOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOfferService {

    @Autowired
    ProductOfferRepository productOfferRepository;
    public void saveProductOffer(ProductOffer productOffer) {
        productOfferRepository.save(productOffer);
    }

    public List<ProductOffer> getAllProductOffers() {
        return productOfferRepository.findAll();
    }

    public void removeProductOffer(Long id) {
        productOfferRepository.deleteById(id);
    }

    public ProductOffer getProductOffersId(Long id) {
        return productOfferRepository.findById(id).get();
    }


    public ProductOffer getProductOffersByProductId(Product product) {
        return productOfferRepository.findProductOfferByProduct(product);
    }

    public ProductOffer getProductOffersById(Long id) {
        return productOfferRepository.findById(id).get();
    }
}
