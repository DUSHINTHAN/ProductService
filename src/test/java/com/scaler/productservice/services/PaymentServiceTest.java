package com.scaler.productservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentGatewaySelector paymentGatewaySelector = Mockito.mock(PaymentGatewaySelector.class);
    private PaymentGateway paymentGateway = Mockito.mock(PaymentGateway.class);
    private PaymentService paymentService = new PaymentService(paymentGatewaySelector);

    @Test
    void createPaymentLink_delegatesToSelectedGateway() {
        when(paymentGatewaySelector.getPaymentGateway()).thenReturn(paymentGateway);
        when(paymentGateway.generatePaymentLink("order-1", 500L, "https://return.url", "idem-1"))
                .thenReturn("https://pay.example.com/link");

        String link = paymentService.createPaymentLink("order-1", 500L, "https://return.url", "idem-1");

        Assertions.assertEquals("https://pay.example.com/link", link);
        verify(paymentGatewaySelector, times(1)).getPaymentGateway();
    }

    @Test
    void createPaymentLink_propagatesGatewayException() {
        when(paymentGatewaySelector.getPaymentGateway()).thenReturn(paymentGateway);
        when(paymentGateway.generatePaymentLink(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("gateway down"));

        Assertions.assertThrows(RuntimeException.class,
                () -> paymentService.createPaymentLink("order-2", 100L, "url", "idem-2"));
    }

}