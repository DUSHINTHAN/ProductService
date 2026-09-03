package com.scaler.productservice.services;

import org.springframework.stereotype.Service;

@Service
public class RazorPaymentGateway implements PaymentGateway{

    @Override
    public String generatePaymentLink(String orderId, Long amount, String returnUrl, String idempotencyKey) {
        // Razorpay requires business KYC verification before enabling
        // payment links, even in test mode — not practical for a personal
        // project. Stub demonstrates extensibility of the PaymentGateway
        // interface; would follow the same pattern as StripePaymentGateway
        // once business verification is complete.

        throw new UnsupportedOperationException("Razorpay integration pending business KYC verification");
    }
}
