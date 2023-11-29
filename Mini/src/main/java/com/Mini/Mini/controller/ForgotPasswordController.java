package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.User;
import com.Mini.Mini.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ForgotPasswordController {

//    @Autowired
//    UserService userService;
//    @Autowired
//    ForgotPasswordService forgotPasswordService;



    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "forgotPassword";
    }

//    @PostMapping("/forgotPassword")
//    public String forgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
//        forgotPasswordService.generatePasswordResetToken(email, request);
//        return "redirect:/forgotPassword";
//    }

//    @PostMapping("/forgotPassword")
//    public String resetPassword(String email,Model model) {
//
//        User user = userService.getUserByEmail(email).get();
//        model.addAttribute("email", user.getEmail());
//        String savedEmail = user.getEmail();
//        if (savedEmail.equals(email)) {
//            return "newPassword";
//
//        }
//
//        return "redirect:/login";
//    }

    @GetMapping("/forgotPassword/newPassword")
    public String newPasswords(){
        return "newPassword";
    }
}






