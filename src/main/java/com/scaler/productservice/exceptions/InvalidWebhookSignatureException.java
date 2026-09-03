package com.scaler.productservice.exceptions;

public class InvalidWebhookSignatureException extends RuntimeException {
    public InvalidWebhookSignatureException(String message) {

        super(message);
    }
}
