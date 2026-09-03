package com.scaler.productservice.controllers;

import com.scaler.productservice.dtos.PaymentRequestDto;
import com.scaler.productservice.services.PaymentService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController  {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/Payments")
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentRequestDto dto) {
        // Logic to initiate payment

        String PaymentLink = paymentService.createPaymentLink(dto.getProductId(), dto.getQuantity(), dto.getReturnUrl(), dto.getIdempotencyKey());

        return new ResponseEntity<>(PaymentLink, HttpStatusCode.valueOf(200));
    }
}
