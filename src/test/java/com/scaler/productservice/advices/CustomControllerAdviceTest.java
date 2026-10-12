package com.scaler.productservice.advices;

import com.scaler.productservice.dtos.ErrorDto;
import com.scaler.productservice.exceptions.InvalidWebhookSignatureException;
import com.scaler.productservice.exceptions.PaymentProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CustomControllerAdviceTest {

    private CustomControllerAdvice customControllerAdvice = new CustomControllerAdvice();

    @Test
    void handleNPEException_returns500WithErrorMessage() {

        ResponseEntity<ErrorDto> response = customControllerAdvice.handleNPEException();

        Assertions.assertEquals(500, response.getStatusCode().value());
        Assertions.assertEquals("Something went wrong. Please try again later.",
                response.getBody().getMessage());
    }

    @Test
    void handleProductNotFoundException_returns404WithErrorMessage() {

        ResponseEntity<ErrorDto> response =
                customControllerAdvice.handleProductNotFoundException();

        Assertions.assertEquals(404, response.getStatusCode().value());
        Assertions.assertEquals(
                "product not found with the given id. Please check the id and try again.",
                response.getBody().getMessage());
    }

    @Test
    void handleInvalidWebhookSignatureException_returns400WithErrorMessage() {

        InvalidWebhookSignatureException exception =
                new InvalidWebhookSignatureException("Invalid Stripe signature");

        ResponseEntity<ErrorDto> response =
                customControllerAdvice.handleInvalidWebhookSignatureException(exception);

        Assertions.assertEquals(400, response.getStatusCode().value());
        Assertions.assertEquals("Invalid Stripe signature",
                response.getBody().getMessage());
    }

    @Test
    void handlePaymentProcessingException_returns500WithErrorMessage() {

        PaymentProcessingException exception =
                new PaymentProcessingException("Payment processing failed");

        ResponseEntity<ErrorDto> response =
                customControllerAdvice.handlePaymentProcessingException(exception);

        Assertions.assertEquals(500, response.getStatusCode().value());
        Assertions.assertEquals("Payment processing failed",
                response.getBody().getMessage());
    }
}