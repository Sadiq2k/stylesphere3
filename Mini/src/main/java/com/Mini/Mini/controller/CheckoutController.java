package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.*;
import com.Mini.Mini.Enam.OrderStatus;
import com.Mini.Mini.repository.OrderIteamRepository;
import com.Mini.Mini.repository.WalletRepository;
import com.Mini.Mini.repository.WalletTransactionRepository;
import com.Mini.Mini.service.*;
import com.Mini.Mini.service.Impl.OrdersService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

@Controller
public class CheckoutController {
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderIteamRepository orderIteamRepository;

    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    CouponService couponService;
    @Autowired
    WalletService walletService;
    @Autowired
    ProductOfferService productOfferService;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    WalletTransactionRepository walletTransactionRepository;

    private double calculateDiscountPrice(Product product) {
        double discountedPrice = product.getDiscountedPrice();
        if (product.getDiscountedPrice() == 0)
            return product.getPrice();
        return discountedPrice;
    }
    private double calculateProductOfferDiscountPrice(ProductOffer productOffer) {
        if (productOffer != null && productOffer.getProduct() != null) {
            return productOffer.getProductDiscountAmount();
        } else {
            return 0.0;
        }
    }

    @GetMapping("/checkout")
    public String checkoutPage(@ModelAttribute("totalAmountApplyCoupon") String totalAmountApplyCoupon,
                               Model model,
                               Principal principal) {
        User user1 = userService.getUserByEmail(principal.getName()).get();

        List<UserAddress> userAddresses1 = userAddressService.getUserAddressesByUserId(user1.getId());

        if (userAddresses1==null){
            return "addAddress";
        }
       try{

            User user = userService.getUserByEmail(principal.getName()).get();
            Cart cart = user.getCart();
            List<CartItem> cartItemList = cart.getCartItems();

            List<UserAddress> userAddresses = userAddressService.getUserAddressesByUserId(user.getId());
            model.addAttribute("userAddress", userAddresses);
            model.addAttribute("cashOnDelivery", paymentService.getPaymentById(user.getId()));
            double totalWithoutDiscount = cartItemList.stream()
                    .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                    .sum();

            double totalWithDiscount = cartItemList.stream()
                    .mapToDouble(item -> item.getQuantity() * calculateDiscountPrice(item.getProduct()))
                    .sum();

            double totalDiscountAmount = cartItemList.stream()
                    .mapToDouble(item -> item.getProduct().getDiscountAmount() * item.getQuantity())
                    .sum();

           double totalWithProductOfferDiscount = cartItemList.stream()
                   .mapToDouble(item -> item.getQuantity() * calculateProductOfferDiscountPrice(item.getProduct().getOffer()))
                   .sum();

            double totalDiscount = totalWithoutDiscount - totalWithDiscount;
           double totalDiscountWithProductOffer = totalWithoutDiscount - totalWithProductOfferDiscount;

           double actual = totalWithoutDiscount - totalDiscount + 40;
           double actualy = totalWithoutDiscount - totalDiscountWithProductOffer + 40;

            model.addAttribute("username", user);
            model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, (a, b) -> a + b));
            model.addAttribute("totalWithoutDiscount", Math.round(totalWithoutDiscount * 100) / 100.0);

            if (totalAmountApplyCoupon.isEmpty() ) {
                model.addAttribute("actual", Math.round(actual * 100) / 100.0);
                model.addAttribute("couponMinus", 0);
            }
            else {
                Double totalAmountApplyCouponDouble = Double.valueOf((totalAmountApplyCoupon));
                model.addAttribute("actual", Math.round(totalAmountApplyCouponDouble * 100) / 100.0);
                model.addAttribute("couponMinus", Math.round((actual-totalAmountApplyCouponDouble)*100)/100.0);
            }
            if (totalWithProductOfferDiscount!=0){
                model.addAttribute("actual", Math.round(actual * 100) / 100.0);
                model.addAttribute("couponMinus", 0);
                model.addAttribute("totalDiscount", Math.round(totalDiscountWithProductOffer * 100) / 100.0);
            }else {
                model.addAttribute("totalWithoutDiscount", Math.round(totalWithoutDiscount * 100) / 100.0);
                model.addAttribute("totalWithDiscount", Math.round(totalWithDiscount * 100) / 100.0);
                model.addAttribute("actual", Math.round(actual * 100) / 100.0);
                model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
                    model.addAttribute("totalDiscount", Math.round(totalDiscount * 100) / 100.0);
            }
            return "checkout";
       } catch (Exception e) {
          e.printStackTrace();
          model.addAttribute("error", "An unexpected error occurred during checkout.");
        return "checkout";
       }
    }

    @PostMapping("/checkout")
    public String placeOrders(@ModelAttribute("paymentMethod") String paymentMethod,
                              @ModelAttribute("address") Long userAddress,
                              @ModelAttribute("couponMinus") Double couponMinus,
                              RedirectAttributes redirectAttributes) {
        if (userAddress==null && userAddress==0){
            redirectAttributes.addFlashAttribute("enterAddress",true);
            return "redirect:/checkout";
        }
        redirectAttributes.addFlashAttribute("address", userAddress);
        redirectAttributes.addFlashAttribute("couponMinus", couponMinus);
        if (paymentMethod.equals("COD")) {
            return "redirect:/COD";
        } else if (paymentMethod.equals("RAZORPAY")) {
            return "redirect:/razorpay";
        }else if (paymentMethod.equals("wallet")){
            return "redirect:/walleted";
        }
        return null;
    }

    @GetMapping("/razorpay")
    public String razorpay(@ModelAttribute("address") Long userAddress,
                           @ModelAttribute("couponMinus") Double couponMinus,
                           Principal principal,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            if (userAddress==null){
                redirectAttributes.addFlashAttribute("enterAddress",true);
                return "redirect:/checkout";
            }
            User user = userService.getUserByEmail(principal.getName()).orElse(null);

            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_oCQnrrG0G7HqHN", "4rzdcya1IF8PlisgFnrPVRG4");
            Cart cart = user.getCart();
            List<CartItem> cartItemList = cart.getCartItems();

            double totalWithoutDiscount = cartItemList.stream()
                    .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                    .sum();

            double totalWithDiscount = cartItemList.stream()
                    .mapToDouble(item -> item.getQuantity() * calculateDiscountPrice(item.getProduct()))
                    .sum();
            double totalWithProductOfferDiscount = cartItemList.stream()
                    .mapToDouble(item -> item.getQuantity() * calculateProductOfferDiscountPrice(item.getProduct().getOffer()))
                    .sum();

            double totalDiscountWithProductOffer = totalWithoutDiscount - totalWithProductOfferDiscount;
            double actualy = totalWithoutDiscount - totalDiscountWithProductOffer + 40;
            double totalDiscount = totalWithoutDiscount - totalWithDiscount;

            double actual = totalWithoutDiscount - totalDiscount + 40 - couponMinus;

            if (totalWithProductOfferDiscount!=0){

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("amount", actual * 100);
                jsonObject.put("currency", "INR");
                Order order = razorpayClient.orders.create(jsonObject);

                model.addAttribute("totalAmount", actual * 100);
                model.addAttribute("orderId", order.get("id"));
                model.addAttribute("address", userAddress);

                return "razorpay";
            }else {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("amount", actual * 100);
                jsonObject.put("currency", "INR");
                Order order = razorpayClient.orders.create(jsonObject);

                model.addAttribute("totalAmount", actual * 100);
                model.addAttribute("orderId", order.get("id"));
                model.addAttribute("address", userAddress);

                return "razorpay";
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
            model.addAttribute("RazorpayError", "An error occurred during Razorpay processing.");
            return "errorPage";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("RazorpayUnexpectedError", "An unexpected error occurred during Razorpay processing.");
            return "errorPage";
        }
    }

    @GetMapping("/ja/{id}")
    public String razorpayProcess(@PathVariable String id,
                                  Model model,
                                  Principal principal) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_oCQnrrG0G7HqHN", "4rzdcya1IF8PlisgFnrPVRG4");
            Payment payment = razorpayClient.payments.fetch(id);
            JSONObject notes = payment.get("notes");
            Long userAddress = notes.getLong("address");

            User user = userService.getUserByEmail(principal.getName()).orElse(null);
            Cart cart = user.getCart();

            Orders orders = new Orders();
            orders.setUser(user);
            orders.setOrderDate(LocalDateTime.now());
            orders.setPaymentMethod("RAZORPAY");
            orders.setStatus(OrderStatus.CONFIRM);
            orders.setAddress(userAddressService.findById(userAddress).get().toString());
            ordersService.saveOrders(orders);


            List<CartItem> cartItemList = cart.getCartItems();
            for (CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice((int) cartItem.getPrice());
                orderItem.setOrder(orders);
                orderItem.setProduct(cartItem.getProduct());

                if (cartItem.getProductVariant() != null) {
                    orderItem.setProductVariant(cartItem.getProductVariant());
                }
                orderIteamRepository.save(orderItem);
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
            double actually = totalWithoutDiscount - totalDiscount + 40;
            if (totalWithProductOfferDiscount!=0){
                String actuall = String.format("%.2f", actually);
                int actuali = payment.get("amount");
                double actual = actuali / 100.0;

                orders.setTotalAmount(actual);
                ordersService.saveOrders(orders);

                model.addAttribute("totalAmount", actual);
                return "OrderSuccess";
            }

            String actuall = String.format("%.2f", actually);
            int actuali = payment.get("amount");
            double actual = actuali / 100.0;

            orders.setTotalAmount(actual);
            ordersService.saveOrders(orders);

            model.addAttribute("totalAmount", actual);
            return "OrderSuccess";
        } catch (RazorpayException e) {
            e.printStackTrace();
            model.addAttribute("RazorpayError", "An error occurred during Razorpay processing.");
            return "redirect:/checkout";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("RazorpayUnexpectedError", "An unexpected error occurred during Razorpay processing.");
            return "redirect:/checkout";
        }
    }

    @GetMapping("/COD")
    public String cod(@ModelAttribute("paymentMethod") String paymentMethod,
                      @ModelAttribute("address") Long userAddress,
                      @ModelAttribute("couponMinus") Double couponMinus,
                      @ModelAttribute("couponCode") String couponCode,
                      Principal principal,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        try {
            if (userAddress==null){
                redirectAttributes.addFlashAttribute("enterAddress",true);
                return "redirect:/checkout";
            }
            User user = userService.getUserByEmail(principal.getName()).orElse(null);
            Cart cart = user.getCart();
            Orders orders = new Orders();
            orders.setUser(user);
            orders.setOrderDate(LocalDateTime.now());
            orders.setPaymentMethod("COD");
            orders.setStatus(OrderStatus.CONFIRM);
            orders.setAddress(userAddressService.findById(userAddress).get().toString());
            ordersService.saveOrders(orders);

            List<CartItem> cartItemList = cart.getCartItems();
            for (CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice((int) cartItem.getPrice());
                orderItem.setOrder(orders);
                orderItem.setProduct(cartItem.getProduct());

                if (cartItem.getProductVariant() != null) {
                    orderItem.setProductVariant(cartItem.getProductVariant());
                }
                orderIteamRepository.save(orderItem);
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

            double totalDiscountWithProductOffer = totalWithoutDiscount - totalWithProductOfferDiscount;
            double actualy = totalWithoutDiscount - totalDiscountWithProductOffer + 40;
            double totalDiscount = totalWithoutDiscount - totalWithDiscount;

            double actual = totalWithoutDiscount - totalDiscount + 40 - couponMinus;

            if (totalWithProductOfferDiscount!=0){
                orders.setTotalAmount(actual);
                ordersService.saveOrders(orders);

                model.addAttribute("totalAmount", Math.round(actual * 100) / 100.0);
                Orders order = ordersService.getOrderById(orders.getId());

                model.addAttribute("order", order.getId());

                return "OrderSuccess";
            }else {
                orders.setTotalAmount(actual);
                ordersService.saveOrders(orders);

                model.addAttribute("totalAmount", Math.round(actual * 100) / 100.0);
                Orders order = ordersService.getOrderById(orders.getId());

                model.addAttribute("order", order.getId());

                return "OrderSuccess";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("CodError", "An unexpected error occurred during COD processing.");
            return "redirect:/checkout";
        }
    }

    @GetMapping("/walleted")
    public String wallet(@ModelAttribute("paymentMethod") String paymentMethod,
                         @ModelAttribute("address") Long userAddress,
                         @ModelAttribute("couponMinus") Double couponMinus,
                         @ModelAttribute("couponCode") String couponCode,
                         RedirectAttributes redirectAttributes,
                         Principal principal, Model model) {
        try {
            if (userAddress==null){
                redirectAttributes.addFlashAttribute("enterAddress",true);
                return "redirect:/checkout";
            }
            User user = userService.getUserByEmail(principal.getName()).orElse(null);
            Cart cart = user.getCart();
            Orders orders = new Orders();
            orders.setUser(user);
            orders.setOrderDate(LocalDateTime.now());
            orders.setPaymentMethod("WALLET");
            orders.setStatus(OrderStatus.CONFIRM);
            orders.setAddress(userAddressService.findById(userAddress).get().toString());
            ordersService.saveOrders(orders);
            Wallet wallet=walletRepository.findWalletByUserId(user.getId());
            List<CartItem> cartItemList = cart.getCartItems();
            for (CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice((int) cartItem.getPrice());
                orderItem.setOrder(orders);
                orderItem.setProduct(cartItem.getProduct());
                    orderItem.setProductVariant(cartItem.getProductVariant());
                orderIteamRepository.save(orderItem);
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

            double totalDiscountWithProductOffer = totalWithoutDiscount - totalWithProductOfferDiscount;
            double actualy = totalWithoutDiscount - totalDiscountWithProductOffer + 40;
            double totalDiscount = totalWithoutDiscount - totalWithDiscount;

            double actual = totalWithoutDiscount - totalDiscount + 40 - couponMinus;
            if (totalWithProductOfferDiscount!=0) {
                double walletAmount = user.getWallet() != null ? user.getWallet().getWalletAmount() : 0.0;

                if (walletAmount >= actualy) {
                    double newWalletAmount = walletAmount - actualy;

                    assert user.getWallet() != null;
                    user.getWallet().setWalletAmount(newWalletAmount);
                    walletService.saveWallet(user.getWallet());
                    orders.setTotalAmount(actualy);
                    ordersService.saveOrders(orders);

                    WalletTransaction walletTransaction= new WalletTransaction();
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    walletTransaction.setWallet(wallet);
                    walletTransaction.setTransaction("Purchase");
                    walletTransaction.setAmount(actualy);
                    walletTransaction.setTransactionType("WALLET");
                    walletTransaction.setDate(String.valueOf(currentDateTime));
                    walletTransactionRepository.save(walletTransaction);

                    model.addAttribute("totalAmount", Math.round(actualy * 100) / 100.0);
                    Orders order = ordersService.getOrderById(orders.getId());
                    model.addAttribute("order", order.getId());
                    return "OrderSuccess";
                }
                return "OrderSuccess";
            }
            double walletAmount = user.getWallet() != null ? user.getWallet().getWalletAmount() : 0.0;
            if (walletAmount >= actual) {
                double newWalletAmount = walletAmount - actual;

                assert user.getWallet() != null;
                user.getWallet().setWalletAmount(newWalletAmount);
                walletService.saveWallet(user.getWallet());
                orders.setTotalAmount(actual);
                ordersService.saveOrders(orders);

                WalletTransaction walletTransaction= new WalletTransaction();
                LocalDateTime currentDateTime = LocalDateTime.now();
                walletTransaction.setWallet(wallet);
                walletTransaction.setTransaction("Purchase");
                walletTransaction.setAmount(actual);
                walletTransaction.setTransactionType("WALLET");
                walletTransaction.setDate(String.valueOf(currentDateTime));
                walletTransactionRepository.save(walletTransaction);

                model.addAttribute("totalAmount", Math.round(actual * 100) / 100.0);
                Orders order = ordersService.getOrderById(orders.getId());
                model.addAttribute("order", order.getId());
                return "OrderSuccess";
            } else {
                redirectAttributes.addFlashAttribute("InsufficientFundsPage", true);
                return "redirect:/checkout";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("InsufficientFundsPage", true);
            return "redirect:/checkout";
        }
    }


    @GetMapping("/applyCoupon")
    public String applyCoupon(@RequestParam("couponCode") String couponCode,
                              Principal principal,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        try{
           List<ProductOffer> productOffer=productOfferService.getAllProductOffers();
             DoubleStream doubleStream = productOffer.stream().mapToDouble(x -> x.getDiscountPercentage());

            Optional<User> optionalUser = userService.getUserByEmail(principal.getName());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Cart cart = user.getCart();
                Optional<Coupon> couponOptional = couponService.getCouponByCode(couponCode);

                if (couponOptional.isPresent()) {
                    Coupon coupon = couponOptional.get();
                    LocalDate currentDate = LocalDate.now();
                    String expirationDateString = String.valueOf(coupon.getExpirationDate());
                    LocalDate expirationDate = LocalDate.parse(expirationDateString);

                    if (expirationDate.isAfter(currentDate)) {
                        double couponAmount = coupon.getDiscountAmount();
                        double totalWithDiscount = cart.getCartItems().stream()
                                .mapToDouble(item -> item.getQuantity() * calculateDiscountPrice(item.getProduct())).sum() + 40;
                        Double totalAmountApplyCoupon = totalWithDiscount - couponAmount;
                        double conditionAmount=coupon.getConditionAmount();

                        if (conditionAmount <= totalWithDiscount) {
                            redirectAttributes.addFlashAttribute("coupon", couponAmount);
                            redirectAttributes.addFlashAttribute("totalAmountApplyCoupon", totalAmountApplyCoupon);
                        } else {
                            redirectAttributes.addFlashAttribute("couponCondition", coupon);
                            redirectAttributes.addFlashAttribute("conditionAmounts", true);
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("couponExpired", "Coupon has expired");
                    }
                } else {
                    redirectAttributes.addFlashAttribute("couponError", "Invalid coupon code");
                }
            }
            return "redirect:/checkout";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("unexpectedError", "An unexpected error occurred during coupon application.");
            return "redirect:/checkout";
        }
    }
}
