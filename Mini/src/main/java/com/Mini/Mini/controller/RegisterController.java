package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Role;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.Wallet;
import com.Mini.Mini.Util.EmailUtil;
import com.Mini.Mini.Util.OtpUtil;
import com.Mini.Mini.repository.RoleRepository;
import com.Mini.Mini.repository.UserRepository;
import com.Mini.Mini.repository.WalletRepository;
import com.Mini.Mini.service.UserService;
import com.Mini.Mini.service.WalletService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegisterController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    OtpUtil otpUtil;
    @Autowired
    WalletService walletService;
    @Autowired
    WalletRepository walletRepository;

    @GetMapping("/register")
    public String registerGet(Model model){
       model.addAttribute("user", new User());
        return "register";
    }
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(false);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") User user,
                               @RequestParam(name = "referralCode", required = false) String referralCode,
                               BindingResult bindingResult,
                               Model model,Principal principal) throws ServletException {

        if (bindingResult.hasErrors()) {
            return "register";
        }
        Optional<User> existing = userService.getUserByEmail(user.getEmail());
        if (existing.isPresent()) {
            if (existing.get().isVerified()) {
                model.addAttribute("registrationError", "You already exists.");
                return "register";
            } else {
                userService.removeUserById(existing.get().getId());
            }
        }

        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
//        List<Role> roles = new ArrayList<>();
//        roles.add(roleRepository.findById(2).get());
        Role role = roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role not found")); // Fetch the role by ID

        user.setActive(true);
        user.setRoles(role);
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setReferralCode(generateReferralCode());
        userRepository.save(user);

        Boolean wallet = walletService.findUserByWallet(user.getId());
        if (!wallet) {
            Wallet wallet1 = new Wallet();
            wallet1.setWalletAmount(0.0);
            wallet1.setUser(user);
            walletRepository.save(wallet1);
        }
        Boolean validationResult = userService.isValidReferralCode(referralCode);
        if (validationResult) {

            User oldUser = userService.getUserByReferralCode(referralCode);
            User user2 = userService.getUserByEmail(user.getEmail()).orElse(null);
            double referralReward = 100.0;
            double referralRewards = 50.0;
            Wallet wallet2 = walletRepository.findWalletByUserId(user2.getId());
            Wallet wallet1 = walletRepository.findWalletByUserId(oldUser.getId());

            double currentWalletAmount = wallet2.getWalletAmount();
            double currentWalletAmounts = wallet1.getWalletAmount();

            wallet2.setWalletAmount(currentWalletAmount + referralReward);
            wallet1.setWalletAmount(currentWalletAmounts + referralRewards);
            walletService.saveWallet(wallet2);
            walletService.saveWallet(wallet1);

            model.addAttribute("email", user.getEmail());
            return "sendOtpRegistered";
        }
        model.addAttribute("email", user.getEmail());
        return "sendOtpRegistered";

    }

    public static String generateReferralCode() {
        String uuid = UUID.randomUUID().toString();

        String referralCode = uuid.replace("-", "").substring(0, 8);

        return referralCode;
    }
    
    @PostMapping("/verifyOtp")
    public String verify(@ModelAttribute("email") String email, @ModelAttribute("otp") String otp,
                         Model model){

        User user = userService.getUserByEmail(email).get();
        String savedOtp = user.getOtp();
        if (savedOtp.equals(otp)) {
            user.setVerified(true);
            userService.saveUser(user);
            return "verified";
        } else {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("wrong", "Wrong OTP Pin.");
            return "sendOtpRegistered";
        }
    }


}
