package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductOffer;
import com.Mini.Mini.dto.ProductOfferDto;
import com.Mini.Mini.service.CategoryService;
import com.Mini.Mini.service.ProductOfferService;
import com.Mini.Mini.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class ProductOfferController {

    @Autowired
    ProductOfferService productOfferService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;


    @GetMapping("/admin/ProductOffers")
    public String showProductOffers(Model model) {
        List<ProductOffer> productOffers=productOfferService.getAllProductOffers();
        Collections.reverse(productOffers);
        model.addAttribute("productOffers", productOffers);
        return "ProductOffer";
    }
    @GetMapping("/admin/addProductOffer")
    public String addProductOffer(Model model) {
        model.addAttribute("productOfferDto", new ProductOfferDto());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        return "addProductOffer";
    }

    @GetMapping("/admin/getProductsByCategory/{categoryId}")
    @ResponseBody
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        return products;
    }
    @PostMapping("/admin/addProductOffer/add")
    public String addProductOffer(@ModelAttribute("ProductOfferDto") ProductOfferDto productOfferDto,
                                  @RequestParam("discountPercentage") double discountPercentage,
                                  @RequestParam("products")Long productId,
                                  Model model){
        Product product = productService.getProductById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
           if (product!=null) {
                ProductOffer productOffer = new ProductOffer();
                productOffer.setProduct(productService.getProductById(productId).get());
                productOffer.setOfferStartDate(productOfferDto.getOfferStartDate());
                productOffer.setOfferEndDate(productOfferDto.getOfferEndDate());
                productOffer.setDiscountPercentage(discountPercentage);
               double originalPrice = product.getPrice();
               double productDiscountAmount = originalPrice * (1 - discountPercentage / 100.0) ;
               productOffer.setProductDiscountAmount(productDiscountAmount);
               productOfferService.saveProductOffer(productOffer);
           }
        return "redirect:/admin/ProductOffers";
    }
    @GetMapping("/admin/productOfferDelete/{id}")
    public String deleteProductOffers(@PathVariable("id") Long id ){
        productOfferService.removeProductOffer(id);
        return "redirect:/admin/ProductOffers";
    }
    @GetMapping("/admin/UpdateProductOffer/{id}")
    public String updateProductOffer(@PathVariable Long id,
                                     Model model){
        ProductOffer productOffer=productOfferService.getProductOffersById(id);
       ProductOfferDto productOfferDto=new ProductOfferDto();
       productOfferDto.setDiscountPercentage(productOffer.getDiscountPercentage());
       productOfferDto.setOfferEndDate(productOffer.getOfferEndDate());
       productOfferDto.setOfferStartDate(productOffer.getOfferStartDate());

        model.addAttribute("productOfferDto",productOfferDto);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        return "UpdateProductOffer";
        }
}
