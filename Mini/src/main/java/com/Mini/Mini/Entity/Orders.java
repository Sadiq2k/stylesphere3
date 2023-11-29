package com.Mini.Mini.Entity;

import com.Mini.Mini.Enam.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CartItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    private Double totalAmount;

    private  String address;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", user=" + user +
                ", items=" + items +
                ", orderItems=" + orderItems +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", address='" + address + '\'' +
                ", paymentMethod=" + paymentMethod +
                '}';
    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentMethod;

    @OneToOne(mappedBy = "orders")
    private Rating rating;

    boolean Active;
    boolean statusChangeCondition;

}
