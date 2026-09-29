package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentGatewaySelector paymentGatewaySelector = Mockito.mock(PaymentGatewaySelector.class);
    private PaymentGateway paymentGateway = Mockito.mock(PaymentGateway.class);
    private ProductService productService = Mockito.mock(ProductService.class);
    private PaymentService paymentService = new PaymentService(paymentGatewaySelector, productService);

    @Test
    void createPaymentLink_delegatesToSelectedGateway() {

        Product product = new Product();
        product.setId(1L);
        product.setPrice(5.0);

        when(productService.getProductDetails(1L)).thenReturn(product);
        when(paymentGatewaySelector.getPaymentGateway()).thenReturn(paymentGateway);
        when(paymentGateway.generatePaymentLink("1", 1000L, "https://return.url", "idem-1"))
                .thenReturn("https://pay.example.com/link");

        String link = paymentService.createPaymentLink("1", 2L, "https://return.url", "idem-1");

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