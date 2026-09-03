package com.scaler.productservice.services;

import org.springframework.stereotype.Service;

@Service
public class RazorPaymentGateway implements PaymentGateway{

    @Override
    public String generatePaymentLink(){

        return null;
    }
}
