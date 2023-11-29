package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Cart;
import com.Mini.Mini.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }


    public void removeById(Long id) {
        cartRepository.deleteById(id);
    }

    public Optional<Cart> getCartItemById(Long itemId) {
        return cartRepository.findById(itemId);
    }


    public List<Cart> clearCart() {
        cartRepository.deleteAll();
        return null;
    }

    public void updateCart(Cart cart1) {
        cartRepository.save(cart1);
    }
}
