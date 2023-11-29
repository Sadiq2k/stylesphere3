package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.Rating;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.repository.OrderRepository;
import com.Mini.Mini.service.ProductService;
import com.Mini.Mini.service.RatingService;
import com.Mini.Mini.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrderRepository orderRepository;

//    @GetMapping("/rating/products")
//    public String showRatingForm(@PathVariable Long productId, Model model) {
//        model.addAttribute("product", productService.getProductById(productId));
//        return "redirect:/userProfile";
//    }

    @GetMapping("/admin/rating/management")
    public String getAdminRatingPage(Model model){
        model.addAttribute("ratings",ratingService.getAllRating());
        return "adminRating";
    }
    @GetMapping("/admin/delete/rating/{id}")
    public String deleteRating(@PathVariable Long id){
        ratingService.removeRating(id);
        return "redirect:/admin/rating/management";
    }
//    @GetMapping("/Rating/Page")
//    public String updateRating(){
//        return "RatingPage";
//    }

    @PostMapping("/rating/products")
    public String submitRating(@RequestParam Long productId,
                               @RequestParam("orderId") Long orderId,
                               @ModelAttribute Rating rating,
                               Model model, Principal principal) {
        User user=userService.getUserByEmail(principal.getName()).get();
        rating.setUser(user);
        rating.setProduct(productService.getProductById(productId).get());
        ratingService.saveRating(rating);

        Orders orders=orderRepository.findById(orderId).get();
        orders.setActive(true);
        orderRepository.save(orders);

        return "redirect:/UserOrders";

    }
}
