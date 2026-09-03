package com.scaler.productservice.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private PaymentGatewaySelector paymentGatewaySelector;

    public PaymentService(PaymentGatewaySelector paymentGatewaySelector) {
        this.paymentGatewaySelector = paymentGatewaySelector;
    }

    public String createPaymentLink(String orderId, Long amount, String returnUrl, String idempotencyKey) {
        // Logic to generate payment link

        return paymentGatewaySelector.getPaymentGateway().generatePaymentLink(orderId, amount, returnUrl, idempotencyKey);
    }

}
