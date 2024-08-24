package com.Mini.Mini.controller;


import com.Mini.Mini.repository.RoleRepository;
import com.Mini.Mini.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

   @GetMapping("/login")
   public String LoginPage() {

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       if(authentication==null || authentication instanceof AnonymousAuthenticationToken) {
           return "login";
       }

       return "redirect:/";
   }


}
