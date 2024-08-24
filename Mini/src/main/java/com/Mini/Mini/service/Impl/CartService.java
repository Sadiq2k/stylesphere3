package com.Mini.Mini.service.Impl;

import com.Mini.Mini.Entity.Cart;
import com.Mini.Mini.Entity.Product;
import com.Mini.Mini.Entity.ProductVariant;
import com.Mini.Mini.Entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

public interface CartService {
    void saveCart(Cart cart1);

    void removeById(Long id);
    Optional<Cart> getCartItemById(Long itemId);
    List<Cart> clearCart();
    void updateCart(Cart cart1);


}
