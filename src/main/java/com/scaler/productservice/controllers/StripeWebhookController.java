package com.scaler.productservice.controllers;

import com.scaler.productservice.exceptions.InvalidWebhookSignatureException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        // Logic to handle webhook events

        try {

            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if("checkout.session.completed".equals(event.getType())) {
                // Handle successful payment
                System.out.println("Payment confirmed for transaction: " + event.getId());
            }

             return new ResponseEntity<>("Success", HttpStatusCode.valueOf(200));
        }
        catch (SignatureVerificationException e){

            throw new InvalidWebhookSignatureException("Invalid webhook signature: " + e.getMessage());
        }

    }
}
