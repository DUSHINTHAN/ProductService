package com.scaler.productservice.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentGatewaySelector {

    private RazorPaymentGateway razorPaymentGateway;
    private StripePaymentGateway stripePaymentGateway;

    public PaymentGatewaySelector(RazorPaymentGateway razorPaymentGateway, StripePaymentGateway stripePaymentGateway) {
        this.razorPaymentGateway = razorPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public PaymentGateway getPaymentGateway(){

        // Logic to select the appropriate payment gateway based on some criteria
        // For example, you can use a configuration property or a request parameter to determine which gateway to use
        //here we return stripe because razorPay has some issues thus we use stripe for now

        return stripePaymentGateway;
    }
}
