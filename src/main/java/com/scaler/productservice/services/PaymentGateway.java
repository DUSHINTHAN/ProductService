package com.scaler.productservice.services;

import org.springframework.stereotype.Service;

@Service
public interface PaymentGateway {

    public String generatePaymentLink();
}
