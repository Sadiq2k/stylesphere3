package com.Mini.Mini.service;

import com.Mini.Mini.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    public Object getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
}
