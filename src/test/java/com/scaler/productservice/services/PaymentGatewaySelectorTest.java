package com.scaler.productservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewaySelectorTest {

    private RazorPaymentGateway razorPaymentGateway = new RazorPaymentGateway();
    private StripePaymentGateway stripePaymentGateway = new StripePaymentGateway();
    private PaymentGatewaySelector selector =
            new PaymentGatewaySelector(razorPaymentGateway, stripePaymentGateway);

    @Test
    void getPaymentGateway_currentlyAlwaysReturnsStripe() {

        PaymentGateway result = selector.getPaymentGateway();

        // we use assertSame here because we want to check that the returned object is the same instance as stripePaymentGateway
        //but assertEquals would only check for equality, not identity and since we are returning the same instance of stripePaymentGateway, assertSame is more appropriate here
        Assertions.assertSame(stripePaymentGateway, result);
    }
}