package com.Mini.Mini.controller;


import com.Mini.Mini.Entity.*;
import com.Mini.Mini.repository.CartItemRepository;
import com.Mini.Mini.repository.CartRepository;
import com.Mini.Mini.repository.OrderIteamRepository;
import com.Mini.Mini.repository.UserAddressRepository;
import com.Mini.Mini.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderIteamRepository orderIteamRepository;

    @Autowired
    UserService userService;
    @Autowired
    UserAddressRepository userAddressRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    OrdersItemService ordersItemService;
    @Autowired
    ProductVariantService productVariantService;
    @Autowired
    CouponService couponService;
    @Autowired
    ProductOfferService productOfferService;


    @GetMapping("/addToCart/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(name = "selectedVariant") String selectedVariant,
                            RedirectAttributes redirectAttributes,
                            Model model, Principal principal) {

        Product product = productService.getProductById(productId).get();
        User user = userService.getUserByEmail(principal.getName()).get();
        ProductVariant productVariant = productVariantService.findSizeByProductVariantId(Long.valueOf(selectedVariant));
        Cart cart = user.getCart();
        Optional<CartItem> cartItemOptional = cartItemRepository.findCartItemByProductAndCart(product, cart);

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            if ( productVariant.equals(cartItem.getProductVariant())) {
                redirectAttributes.addFlashAttribute("alreadyInCart",true);
                return "redirect:/shop/viewproduct/" + productId;
            }
            else {
                CartItem cartItem1 = new CartItem();
                cartItem1.setProduct(product);
                cartItem1.setCart(cart);
                cartItem1.setPrice(product.getPrice());
                cartItem1.setProductVariant(productVariant);
                cartItem1.setQuantity(1);
                cartItemRepository.save(cartItem1);

            }
        }else {
            CartItem cartItem1 = new CartItem();
            cartItem1.setProduct(product);
            cartItem1.setCart(cart);
            cartItem1.setPrice(product.getPrice());
            cartItem1.setProductVariant(productVariant);
            cartItem1.setQuantity(1);
            cartItemRepository.save(cartItem1);

        }
        return "redirect:/shop/viewproduct/" + productId;
    }



//    private double applyDiscount(double originalPrice, double discountPercentage) {
//        double discountMultiplier = 1 - (discountPercentage / 100.0);
//        return originalPrice * discountMultiplier;
//    }


    @GetMapping("/cart/increaseQuantity/{itemId}")
    public String increaseQuantity(@PathVariable Long itemId) {
        Optional<CartItem> cartItemOptional = cartItemService.getCartItemById(itemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/decreaseQuantity/{itemId}")
    public String decreaseQuantity(@PathVariable Long itemId) {
        Optional<CartItem> cartItemOptional = cartItemService.getCartItemById(itemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemRepository.save(cartItem);
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/removeItem/{itemId}")
    public String cartItemRemove(@PathVariable Long itemId) {
        cartItemService.removeCartById(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartGet(Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName()).get();
        Cart cart = user.getCart();

        List<CartItem> cartItemList;
        if (cart == null) {
            Cart cart1 = new Cart();
            cart1.setUser(user);
            user.setCart(cart1);
            cartService.saveCart(cart1);
            cartItemList = cart1.getCartItems();
        } else {
            cartItemList = cart.getCartItems();

        }
        double totalWithoutDiscount = cartItemList.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        double totalWithDiscount = cartItemList.stream()
                .mapToDouble(item -> item.getQuantity() * calculateDiscountPrice(item.getProduct()))
                .sum();
        double totalWithProductOfferDiscount = cartItemList.stream()
                .mapToDouble(item -> item.getQuantity() * calculateProductOfferDiscountPrice(item.getProduct().getOffer()))
                .sum();
        double totalDiscount = totalWithoutDiscount - totalWithDiscount;
        double actual = totalWithoutDiscount - totalDiscount + 40;
        double totalDiscountWithProductOffer = totalWithoutDiscount - totalWithProductOfferDiscount;
        double actualy = totalWithoutDiscount - totalDiscountWithProductOffer + 40;

        if(totalWithProductOfferDiscount!=0){

            model.addAttribute("username", user);
            model.addAttribute("cart", cartItemList);
            model.addAttribute("totalWithoutDiscount", Math.round(totalWithoutDiscount * 100) / 100.0);
            model.addAttribute("totalWithDiscount", Math.round(totalWithDiscount * 100) / 100.0);
            model.addAttribute("totalDiscount", Math.round(totalDiscountWithProductOffer * 100) / 100.0);
            model.addAttribute("actual", Math.round(actualy * 100) / 100.0);
            model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
        }
        model.addAttribute("username", user);
        model.addAttribute("cart", cartItemList);
        model.addAttribute("totalWithoutDiscount", Math.round(totalWithoutDiscount * 100) / 100.0);
        model.addAttribute("totalWithDiscount", Math.round(totalWithDiscount * 100) / 100.0);
        model.addAttribute("actual", Math.round(actual * 100) / 100.0);
        model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
        if (totalWithProductOfferDiscount==0){
            model.addAttribute("totalDiscount", Math.round(totalDiscount * 100) / 100.0);

        }
        return "cart";
    }

    private double calculateDiscountPrice(Product product) {
        double discountedPrice = product.getDiscountedPrice();
        if (product.getDiscountedPrice() == 0)
            return product.getPrice();
        return discountedPrice;
    }
//    private double calculateProductOfferDiscountPrice(ProductOffer productOffer) {
//        double discountedPrice = productOffer.getProductDiscountAmount();
//        if (productOffer.getProductDiscountAmount() == 0)
//            return productOffer.getProduct().getPrice();
//        return discountedPrice;
//    }
private double calculateProductOfferDiscountPrice(ProductOffer productOffer) {
    if (productOffer != null && productOffer.getProduct() != null) {
        return productOffer.getProductDiscountAmount();
    } else {
        return 0.0;
    }
}




}





