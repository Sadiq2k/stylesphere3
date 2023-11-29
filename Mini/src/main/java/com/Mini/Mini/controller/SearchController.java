package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.service.CategoryService;
import com.Mini.Mini.service.ProductService;
import com.Mini.Mini.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String getSearchResults(@RequestParam String keyword, Model model) {
        User user = userService.findUserByfirstname(keyword);
        if (user != null) {
            model.addAttribute("userManagement", user);
        } else {
            // User not found, you can display a message or handle it as needed
            model.addAttribute("userNotFound", "User not found for the given keyword: " + keyword);
        }
        return "userManagement";
    }

    @GetMapping("/shop/searchProduct")
    public String searchProduct(@RequestParam(name = "name", required = false) String keyword, Model model) {
        List<Product> searchResults = new ArrayList<>();
        String errorMessage = null;
        if (keyword != null && !keyword.isEmpty()) {
            searchResults = productService.ignoreCaseForSearch(keyword);
            if (searchResults.isEmpty()) {
                errorMessage = "No results found for your search query.";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("products", searchResults);
        model.addAttribute("categories",categoryService.getAllCategory());


        return "shop";
    }
}
