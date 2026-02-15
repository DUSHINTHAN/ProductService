package com.scaler.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FakeStoreCreateProductDto {

    private String title;
    private String description;
    private String image;
    private double price;
    private String category;
}
