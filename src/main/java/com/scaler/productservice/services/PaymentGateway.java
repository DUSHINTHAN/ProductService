package com.scaler.productservice.services;


public interface PaymentGateway {

    public String generatePaymentLink(String orderId, Long amount, String returnUrl, String idempotencyKey);
}
