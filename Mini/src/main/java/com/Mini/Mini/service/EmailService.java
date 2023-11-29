//package com.Mini.Mini.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
////    @Value("${spring.mail.from}") // Define the 'from' email address in application.properties
//    private String fromEmail;
//
//    public void sendPasswordResetEmail(String toEmail, String resetLink) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(fromEmail); // Set the 'from' email address
//        mailMessage.setTo(toEmail);
//        mailMessage.setSubject("Password Reset Request");
//        mailMessage.setText("To reset your password, click the following link:\n" + resetLink);
//
//        javaMailSender.send(mailMessage);
//    }
//}
