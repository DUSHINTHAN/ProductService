package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private PaymentGatewaySelector paymentGatewaySelector;

    private ProductService productService;

    public PaymentService(PaymentGatewaySelector paymentGatewaySelector, @Qualifier("databaseProductService") ProductService productService) {
        this.productService = productService;
        this.paymentGatewaySelector = paymentGatewaySelector;
    }

    public String createPaymentLink(String productId, Long quantity, String returnUrl, String idempotencyKey) {
        // Logic to generate payment link

        Product product = productService.getProductDetails(Long.parseLong(productId));
        long amountInPaise = Math.round(product.getPrice() * 100) * quantity; // Convert price to paise and multiply by quantity

        return paymentGatewaySelector.getPaymentGateway().generatePaymentLink(productId, amountInPaise, returnUrl, idempotencyKey);
    }

}
