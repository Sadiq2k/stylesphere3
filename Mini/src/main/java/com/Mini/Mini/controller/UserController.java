package com.Mini.Mini.controller;

import  com.Mini.Mini.Entity.*;
import com.Mini.Mini.dto.UserAddressDto;
import com.Mini.Mini.repository.OrderIteamRepository;
import com.Mini.Mini.repository.WalletRepository;
import com.Mini.Mini.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController{

    @Autowired
    UserAddressService userAddressService;
    @Autowired
    UserService userService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrdersItemService ordersItemService;
    @Autowired
    ProductService productService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    OrderIteamRepository ordersItemRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    RatingService ratingService;

    @GetMapping("/userProfile")
    public String userProfile(Model model,Principal principal){
       User user = userService.getUserByEmail(principal.getName()).orElse(null);
       List<UserAddress> userAddresses = userAddressService.getUserAddressesByUserId(user.getId());
       List<Orders> orders = ordersService.getOrderByUser(user);
       model.addAttribute("orders", orders);

       Boolean wallet=walletService.findUserByWallet(user.getId());
       if (!wallet){

           Wallet wallet1= new Wallet();
           wallet1.setWalletAmount(1.0);
           wallet1.setUser(user);
           walletRepository.save(wallet1);
       }
       Cart cart = user.getCart();
       if (cart != null) {
           model.addAttribute("cartCount", cart.getCartItems().stream().map(cartItem -> cartItem.getProduct()).distinct().count());
       }
       model.addAttribute("userAddress",userAddresses);
       model.addAttribute("username",user);
       return "userProfile";
    }


    @PostMapping("/userProfile")
    public String UpdateUserProfile(@RequestParam("firstname") String firstname,
                                    @RequestParam("lastname") String lastname,
                                    Model model,Principal principal){

        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        if (user!=null){
            user.setFirstname(firstname);
        }
       if (user!=null){
           user.setLastname(lastname);
       }

        userService.updateUser(user);

       return "redirect:/userProfile";
    }

    @GetMapping("/addAddress")
    public String showAddressForm(Model model) {
        model.addAttribute("userAddress", new UserAddress());
        return "addAddress";
    }

    @PostMapping("/addAddress")
    public String saveAddress(@Valid @ModelAttribute("userAddress") UserAddress userAddress, BindingResult bindingResult, Principal principal) {
       User user = userService.getUserByEmail(principal.getName()).orElse(null);
        if (bindingResult.hasErrors()) {
            return "addAddress";
        }
       if (user != null) {
           userAddress.setUser(user);
           userAddressService.saveUserAddress(userAddress);
        return "redirect:/checkout";
       }
       return "checkout";
   }

    @GetMapping("/userProfileToAddAddress")
    public String showAddressFormUserProfile(Model model) {
        model.addAttribute("userAddress", new UserAddress());
        return "userProfileToAddAddress";
    }
    @PostMapping("/userProfileToAddAddress/add")
    public String saveAddressUserProfile(@Valid @ModelAttribute("userAddress") UserAddress userAddress,
                                         BindingResult bindingResult,
                                         Principal principal) {
        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        if (bindingResult.hasErrors()) {
            return "userProfileToAddAddress";
        }
      if (user!=null){
          userAddress.setUser(user);
            userAddressService.saveUserAddress(userAddress);
        }
        return "redirect:/userProfile";
    }

    @GetMapping("/deleteAddress/{id}")
    public String deleteUserAddress(@PathVariable Long id){
       userAddressService.removeUserAddressById(id);
       return "redirect:/userProfile";
    }
    @GetMapping("/userProfileToUpdateAddress/{id}")
    public String updateUserAddress(@PathVariable Long id,Model model){
     UserAddress userAddress=  userAddressService.getUserAddressById(id).get();
        UserAddressDto userAddressDto=new UserAddressDto();
        userAddressDto.setId(userAddress.getId());
        userAddressDto.setCity(userAddress.getCity());
        userAddressDto.setAddress(userAddress.getAddress());
        userAddressDto.setCountry(userAddress.getCountry());
        userAddressDto.setState(userAddress.getState());
        userAddressDto.setUsername(userAddress.getUsername());
        userAddressDto.setLandmark(userAddress.getLandmark());
        userAddressDto.setPincode(userAddress.getPincode());
        userAddressDto.setPhoneNo(userAddress.getPhoneNo());
        userAddressDto.setAddressType(userAddress.getAddressType());
        model.addAttribute("userAddress",userAddressDto);
       return "userProfileToAddAddress";
    }

    @GetMapping("/updatePassword")
    public String showUpdatePasswordPage(){
       return "updatePassword";
    }

    @PostMapping("/updatePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword")String confirmPassword,
                                 Principal principal,Model model) {
        User user = userService.getUserByEmail(principal.getName()).get();
        if (!Objects.equals(newPassword, confirmPassword)) {
            model.addAttribute("errConfirmPassword", "New password and confirm password must match");
            return "updatePassword";
        }
        if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            String hashedPassword = bCryptPasswordEncoder.encode(newPassword);
            model.addAttribute("errCurrentPassword", "Incorrect current password");
            user.setPassword(hashedPassword);
            userService.updateUser(user);
            model.addAttribute("passwordSuccess", "Password changed successfully");
            return "updatePassword";
        }
        return "updatePassword";
    }

}
