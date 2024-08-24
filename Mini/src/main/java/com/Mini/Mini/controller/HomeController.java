package com.Mini.Mini.controller;
import com.Mini.Mini.Entity.*;
import com.Mini.Mini.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.OptionalDouble;

@Controller
public class HomeController {
    @Autowired
    CategoryService categoryService;

  private   ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    VariantsService variantsService;
    @Autowired
    ProductVariantService productVariantService;
    @Autowired
    ProductOfferService productOfferService;
    @Autowired
    RatingService ratingService;


    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/"})
    public String home(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName()).orElse(null);
            if (user != null) {
                Cart cart = user.getCart();
                if (cart != null) {
                    model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
                }
                model.addAttribute("username", user);
            }
        }
        return "index";
    }

    @GetMapping("/shop")
    public String Shop(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName()).get();

            Cart cart = user.getCart();
            if (cart != null) {
                model.addAttribute("username", user);
                model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
            }
        }
        List<Category> categoriesWithDiscount = categoryService.getAllCategoriesWithDiscount();
        List<Product> products = productService.getAllProduct();



        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());

        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String showProductByCategory(@PathVariable Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductByCategoryID(id));

        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName()).get();
          Product productId=  productService.getProductById(Long.valueOf(id)).get();
//          Rating rating=RatingService.getRatingByProductId(productId);
            List<Rating> rating=ratingService.getAllRatingsCount();
//             long yourRatingCountsListOrArray = rating.stream().map(x -> x.getRating()).count();
           List<Rating> yourRatingCountsListOrArray=ratingService.getRatingCountByProductId( id);

            OptionalDouble va = yourRatingCountsListOrArray.stream()
                    .mapToDouble(Rating::getRating)
                    .average();
            double roundedAverage = Math.round(va.orElse(0.0) * 100) / 100.0;

            int totalDiscountAmount = (int) yourRatingCountsListOrArray.stream()
                    .mapToDouble(item -> item.getRating())
                    .sum();
            Cart cart = user.getCart();
            if (cart != null) {
                model.addAttribute("username", user);
                model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
            }

            model.addAttribute("product", productService.getProductById(Long.valueOf(id)).get());
            model.addAttribute("ratingCounts",roundedAverage);
            model.addAttribute("totalDiscountAmount",totalDiscountAmount);
        }
        return "viewProduct";
    }
}