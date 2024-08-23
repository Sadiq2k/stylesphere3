package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Entity
@Setter
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column( name = "firstname")
    @Size(min = 1, message = "is required")
    private String firstname;


    @Column(nullable = false, name = "lastname")
    @NotNull
    @Size(min = 1, message = "is required")
    private String lastname;


    @Column(unique = true)
    @Size(min = 1,message = "is required")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotNull
    @Column(name = "password")
    @Size(min = 1, message = "is required")
    private String password;

//    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
//            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
//    )
//    private List<Role> roles;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role roles;



    //user and cart mapping together--------------------------
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    private boolean Active;
    private boolean verified;
    private String otp;
    private LocalDateTime otpGeneratedTime;

    // user and user_Address mapping------------------------
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAddress> userAddress;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Orders> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wallet> wallets;

    @Column(unique = true)
    private String referralCode;

    @OneToOne
    @JoinColumn(name = "id")
    private Wallet wallet;

    public User() {
    }
    public User(User user) {
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();

    }


}