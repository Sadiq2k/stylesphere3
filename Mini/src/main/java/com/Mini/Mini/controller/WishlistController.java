package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Cart;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.Wishlist;
import com.Mini.Mini.repository.WishlistRepository;
import com.Mini.Mini.service.ProductService;
import com.Mini.Mini.service.UserService;
import com.Mini.Mini.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class WishlistController {

    @Autowired
    ProductService productService;
    @Autowired
    WishlistService wishlistService;
    @Autowired
    UserService userService;
    @Autowired
    WishlistRepository wishlistRepository;

    @GetMapping("/addToWishList/{productId}")
    public String addToWishList(@PathVariable("productId") Long productId,
                                Principal principal,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        User user = userService.getUserByEmail(principal.getName()).orElse(null);

        // Check if the product with productId is already in the user's wishlist
        boolean isProductInWishlist = wishlistService.isProductInWishlist(user.getId(), productId);

        if (isProductInWishlist) {
            redirectAttributes.addFlashAttribute("productId",productId);
            redirectAttributes.addFlashAttribute("wishlist", "This product already added in Wishlist");
        } else {
            Product product = productService.getProductById(productId).orElse(null);

            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(product);
            wishlist.setUser(user);
            wishlistService.saveWishlist(wishlist);
        }
            return "redirect:/shop";

    }


    @GetMapping("wishlist")
    public String getWishlist(Model model,Principal principal){
        User user = userService.getUserByEmail(principal.getName()).orElse(null);

        model.addAttribute("wishlist",wishlistService.getAllWishlist());
        model.addAttribute("username", user);

        return "Wishlist";
    }
    @GetMapping("/admin/wishlist/delete/{id}")
    public String deleteWishlist(@PathVariable("id")Long id){
        wishlistService.removeWishlist(id);
        return "redirect:/wishlist";
    }
    @GetMapping("/wishlist/viewproduct/{id}")
    public String getViewProduct(@PathVariable("id")Long id, Principal principal,Model model){
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName()).get();
            Cart cart = user.getCart();
            if (cart != null) {
                model.addAttribute("username", user);
                model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
            }
            model.addAttribute("product", productService.getProductById(Long.valueOf(id)).get());
        }
        return "viewProduct";

    }

}
