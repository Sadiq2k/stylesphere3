package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Category;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.repository.ProductRepository;
import com.Mini.Mini.service.CategoryService;
import com.Mini.Mini.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryOfferController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/admin/discount")
    public String showDiscountForm(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categories", categories);
        return "CategoryDiscount";
    }


    @PostMapping("/admin/discount")
    public String applyDiscount(@RequestParam Long categoryId,
                                @RequestParam double discountPercentage,
                                Model model) {
        Category category = categoryService.findCategoryById(Math.toIntExact(categoryId)).orElse(null);

        if (category != null) {
            List<Product> products = productService.getAllProductByCategoryID(categoryId);
            productService.updateProductPrices(products, discountPercentage);

            category.setDiscountPercentage(discountPercentage);
            categoryService.addCategories(category);


            model.addAttribute("products", products);
            model.addAttribute("categoryName", category.getName());
            model.addAttribute("discountPercentage",discountPercentage);

            return "redirect:/admin/discount";
        } else {
            return "redirect:/admin/discount";
        }
    }
    @GetMapping("/deleteCategoryOffer/{categoryId}")
    public String deleteCategoryOffer(@PathVariable("categoryId") Long categoryId){
        Category category = categoryService.findDiscountPercentageById(categoryId);
        if (category != null) {
            List<Product> products = productService.getAllProductByCategoryID(categoryId);
            for (Product product : products) {
                product.setDiscountedPrice(0.0);
                productService.saveProduct(product);
                category.setDiscountPercentage(0.0);
                categoryService.saveCategory(category);
            }
        }
        return "redirect:/admin/discount";
    }
}
