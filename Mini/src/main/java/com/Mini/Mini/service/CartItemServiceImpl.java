package com.Mini.Mini.service;

import com.Mini.Mini.Entity.CartItem;
import com.Mini.Mini.repository.CartItemRepository;
import com.Mini.Mini.service.Impl.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    CartItemRepository  cartItemRepository;

    public void removeCartById(Long Item){
        cartItemRepository.deleteById(Item);
    }


    public Optional<CartItem> getCartItemById(Long itemId) {
        return cartItemRepository.findById(itemId);
    }

    public void clearAllCartItems() {
        cartItemRepository.deleteAll();
    }

    public void removeCart(List<CartItem> cartItems) {
        cartItemRepository.deleteAll();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItemRepository.deleteAll();
    }
}
