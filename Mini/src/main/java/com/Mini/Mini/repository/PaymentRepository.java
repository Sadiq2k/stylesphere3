package com.Mini.Mini.repository;


import com.Mini.Mini.Entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentMethod,Long> {
}
