package com.scaler.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    private String productId;
    private Long quantity;
    private String returnUrl;
    private String idempotencyKey;
}
