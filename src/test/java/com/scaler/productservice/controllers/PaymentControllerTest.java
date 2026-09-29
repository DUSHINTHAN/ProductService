package com.scaler.productservice.controllers;


import com.scaler.productservice.dtos.PaymentRequestDto;
import com.scaler.productservice.exceptions.PaymentProcessingException;
import com.scaler.productservice.services.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    private PaymentService paymentService = Mockito.mock(PaymentService.class);
    private PaymentController paymentController = new PaymentController(paymentService);

    @Test
    void initiatePayment_returns200WithPaymentLink() {

        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setProductId("prod-1");
        dto.setQuantity(2L);
        dto.setReturnUrl("https://return.url");
        dto.setIdempotencyKey("idem-1");


        when(paymentService.createPaymentLink("prod-1", 2L, "https://return.url", "idem-1"))
                .thenReturn("https://pay.example.com/abc");

        ResponseEntity<String> response = paymentController.initiatePayment(dto);

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("https://pay.example.com/abc", response.getBody());
    }

    @Test
    void initiatePayment_gatewayFailure_throwsPaymentProcessingException() {

        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setProductId("prod-2");
        dto.setQuantity(1L);
        dto.setReturnUrl("https://return.url");
        dto.setIdempotencyKey("idem-2");

        when(paymentService.createPaymentLink(any(), any(), any(), any()))
                .thenThrow(new PaymentProcessingException("Failed to generate payment link"));

        Assertions.assertThrows(PaymentProcessingException.class,
                () -> paymentController.initiatePayment(dto));
    }


}