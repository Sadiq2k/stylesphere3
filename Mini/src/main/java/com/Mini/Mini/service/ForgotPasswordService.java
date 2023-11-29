//package com.Mini.Mini.service;
//
//import com.Mini.Mini.Entity.ForgotPasswordToken;
//import com.Mini.Mini.Entity.User;
//import com.Mini.Mini.repository.ForgotPasswordTokenRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class ForgotPasswordService {
//    @Autowired
//    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private EmailService emailService; // Service for sending emails
//
//    @Value("${token.expiration.minutes}")
//    private int tokenExpirationMinutes;
//
//    public void generatePasswordResetToken(String email, HttpServletRequest request) {
//        Optional<User> user = userService.getUserByEmail(email);
//
//        if (user == null) {
//            // Handle the case where the user does not exist
//            return;
//        }
//
//        // Generate a unique token and set its expiration time
//        String token = UUID.randomUUID().toString();
//        Date expirationTime = new Date(System.currentTimeMillis() + tokenExpirationMinutes * 60 * 1000);
//
//        // Create a new PasswordResetToken and save it
//        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(token, user, expirationTime);
//        forgotPasswordTokenRepository.save(forgotPasswordToken);
//
//        // Send an email with the reset link
//        String resetLink = getResetLink(request, token);
//        emailService.sendPasswordResetEmail(String.valueOf(userService.getUserByEmail(email).get()), resetLink);
//    }
//
//    // Helper method to construct the reset link
//    private String getResetLink(HttpServletRequest request, String token) {
//        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        return appUrl + "/reset-password/" + token;
//    }
//
//}
