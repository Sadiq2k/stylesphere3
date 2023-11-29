package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Coupon;
import com.Mini.Mini.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id).get();
    }

    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }


    public Optional<Coupon> getCouponByCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode);
    }

    public void removeCoupon(Long id) {
        couponRepository.deleteById(id);
    }


    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
