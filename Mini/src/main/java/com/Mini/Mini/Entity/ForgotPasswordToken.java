//package com.Mini.Mini.Entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.Date;
//import java.util.Optional;
//
//@Getter
//@Setter
//@Entity
//public class ForgotPasswordToken {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String token;
//
//    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "user_id")
//    private Optional<User> user;
//
//    private Date expirationTime;
//
//    public ForgotPasswordToken(String token, Optional<User> user, Date expirationTime) {
//        this.expirationTime=expirationTime;
//        this.token=token;
//        this.user=user;
//    }
//}
