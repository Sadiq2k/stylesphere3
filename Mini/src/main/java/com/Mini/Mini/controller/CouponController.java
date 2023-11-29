package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Coupon;
import com.Mini.Mini.dto.CouponDto;
import com.Mini.Mini.repository.CouponRepository;
import com.Mini.Mini.service.CouponService;
import com.Mini.Mini.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CouponController {

    @Autowired
   CouponService couponService;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserService userService;


    @GetMapping("/admin/couponAdd")
    public String showCouponAddPage(Model model) {
        model.addAttribute("coupons", new Coupon());
        return "CouponAdd";
    }

    @PostMapping("/admin/couponAdd")
    public String postCoupon(@ModelAttribute("coupons") Coupon coupon){

        couponRepository.save(coupon);

        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteCoupon(@PathVariable("id")Long id){
        couponService.removeCoupon(id);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/coupons")
    public String getCoupons(Model model) {
        List<Coupon> coupons = couponRepository.findAll();
        model.addAttribute("coupons", coupons);
        return "Coupons";
    }

    @GetMapping("/admin/couponUpdate/{id}")
    public String getUpdateCoupon(@PathVariable("id")Long id,Model model){

        Coupon coupons=couponService.getCouponById(id);

        model.addAttribute("coupons",coupons);
        return "UpdateCoupons";
    }
    @PostMapping("/admin/couponUpdate")
    public String postUpdateCoupons(@Valid @ModelAttribute("coupons") CouponDto couponDto) {
//        if (bindingResult.hasErrors()) {
//            return "/admin/couponUpdate";
//        }

        Coupon coupon = couponService.getCouponById(couponDto.getId());
        if (coupon != null) {
            coupon.setCouponCode(couponDto.getCouponCode());
            coupon.setDiscountAmount(couponDto.getDiscountAmount());
            coupon.setExpirationDate(couponDto.getExpirationDate());
            coupon.setConditionAmount(couponDto.getConditionAmount());

            couponService.saveCoupon(coupon);
        }
        return "redirect:/admin/coupons";
    }

    @GetMapping("/user/coupons")
    public String getUserCoupon(Model model) {
        List<Coupon> coupons = couponService.getAllCoupons();

        List<Coupon> validCoupons = new ArrayList<>();
        List<Coupon> invalidCoupons = new ArrayList<>();

        // Check validity and add to appropriate list
        for (Coupon coupon : coupons) {
            if (isValidCoupon(LocalDate.parse(coupon.getExpirationDate()))) {
                validCoupons.add(coupon);
            } else {
                invalidCoupons.add(coupon);
            }
        }

        model.addAttribute("validCoupons", validCoupons);
        model.addAttribute("invalidCoupons", invalidCoupons);

        model.addAttribute("coupons", coupons);

        return "UserCoupon";
    }

    private boolean isValidCoupon(LocalDate expirationDate) {
        return LocalDate.now().isBefore(expirationDate);
    }

}
