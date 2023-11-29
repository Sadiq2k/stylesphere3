package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductImage;
import com.Mini.Mini.repository.ProductImageRepository;
import com.Mini.Mini.repository.ProductOfferRepository;
import com.Mini.Mini.repository.ProductRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryService categoryService;

   private ProductService productService;
   @Autowired
   ProductOfferService productOfferService;
   @Autowired
    ProductOfferRepository productOfferRepository;

   private final  ProductImageRepository productImageRepository;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }


    @Autowired
    public ProductService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public List<ProductImage> getProductImageIdsByProductId(ProductImage productId) {
        return productImageRepository.findIdsByProduct_Id(productId);
    }
    public void removeProductById(Long productId) {

        productRepository.deleteById(productId);
    }
    public Optional<Product> getProductById(@PathVariable Long id){
        return productRepository.findById(id);
    }
    public List<Product> getAllProductByCategoryID(Long id){
        return productRepository.findAllByCategory_Id(id);
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public Object getUserIdByProduct(Long id) {
        return productRepository.findById(id);
    }

    public Object getProductByUser(Long id) {
        return productRepository.findById(id);
    }
    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public List<Product> ignoreCaseForSearch(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);}


    public void applyCategoryDiscount(Product product) {
        double categoryDiscount = categoryService.getCategoryDiscountPercentage(product.getCategory().getId());
        double discountedPrice = product.getPrice() - (product.getPrice() * categoryDiscount);
        product.setPrice(discountedPrice);
    }

    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findAllByCategory_Id(categoryId);
    }


    public void updateProductPrices(List<Product> products, double percentage) {
        for (Product product : products) {
            double currentPrice = product.getPrice();
            double newPrice = currentPrice * (1 - (percentage / 100.0));
            product.setDiscountedPrice(newPrice);
            productRepository.save(product);
        }
    }

}
