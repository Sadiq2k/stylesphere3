package com.Mini.Mini.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "Product")
@Setter
@Getter
public class Product{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @NotNull(message = "is required")
        private String name;

        @ManyToOne(fetch =FetchType.LAZY)
        @JoinColumn(name = "Categories_id", referencedColumnName = "Categories_id",nullable = false)
        private Category category;

        private String imageName;

        private String Description;

        @NotNull
        private Double Price;

        @NotNull
        @Column(name = "discounted_price")
        private double discountedPrice;

        @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
        private List<CartItem> cartItem;

        @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
        private List<OrderItem> orderItems;

        @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
        public List<ProductImage> images ;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<ProductVariant> variants;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<Rating> ratings;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<ProductOffer> productOffers;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<Wishlist> wishlists;

        private Long UnitsInStock;

        @Override
        public String toString() {
                return "Product{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", category=" + category +
                        ", imageName='" + imageName + '\'' +
                        ", Description='" + Description + '\'' +
                        ", Price=" + Price +
                        ", cartItem=" + cartItem +
                        ", orderItems=" + orderItems +
                        ", UnitsInStock=" + UnitsInStock +
                        ", images=" + images +
                        '}';
        }



        public double getDiscountAmount() {
                if (productOffers != null && !productOffers.isEmpty() && productOffers.get(0).isActive()) {
                        return productOffers.get(0).getProductDiscountAmount();
                } else {
                        return 0.0;
                }
        }

        public Product(){}

        public ProductOffer getOffer() {
                List<ProductOffer> productOfferList = productOffers.stream().filter(ProductOffer::isActive).toList();
                if (productOfferList.isEmpty())
                        return null;
                else
                        return productOfferList.get(0);
        }


}
