package com.Mini.Mini.Entity;

import com.Mini.Mini.Enam.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

//    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<CartItem> items;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    private Double totalAmount;

    private  String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentMethod;

    @OneToOne
    private Orders orders;


}
