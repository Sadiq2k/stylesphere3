package com.Mini.Mini.service.Impl;

import com.Mini.Mini.Entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    Optional<CartItem> getCartItemById(Long itemId);

    void removeCartById(Long itemId);
    void clearAllCartItems();
    void removeCart(List<CartItem> cartItems);
    void removeCartItem(CartItem cartItem);
}
