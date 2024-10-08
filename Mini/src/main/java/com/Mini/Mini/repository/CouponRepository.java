package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long> {


    Optional<Coupon> findByCouponCode(String couponCode);
}
