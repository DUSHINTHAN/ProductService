package com.scaler.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequestDto {

    private String title;
    private String description;
    private String image;
    private double price;
    private String category;
}
