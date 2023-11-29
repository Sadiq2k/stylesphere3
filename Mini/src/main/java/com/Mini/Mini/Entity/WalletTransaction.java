package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private String transaction;
    private double amount;
    private String transactionType;
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
