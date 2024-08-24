package com.Mini.Mini.service;

import com.Mini.Mini.Entity.*;
import com.Mini.Mini.repository.CartItemRepository;
import com.Mini.Mini.repository.CartRepository;
import com.Mini.Mini.service.Impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

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
