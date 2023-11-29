package com.Mini.Mini.service;

import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.Wishlist;
import com.Mini.Mini.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {
    @Autowired
    WishlistRepository wishlistRepository;
    public void saveWishlist(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }

    public Object getAllWishlist() {
        return wishlistRepository.findAll();
    }

    public void removeWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }

    public List<Wishlist> getProductByUser(Long id) {
        return wishlistRepository.findIdByUserId(id);
    }

    public boolean isProductInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

}
