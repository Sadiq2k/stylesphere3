package com.Mini.Mini.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Rating {

    @Id
    @Column(name = "id" ,nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comment;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(orphanRemoval = true)
    @JoinTable(name = "Rating_orders",
            joinColumns = @JoinColumn(name = "rating_id"),
            inverseJoinColumns = @JoinColumn(name = "orders_id"))
    private Orders orders;

}
