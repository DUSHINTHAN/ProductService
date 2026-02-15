package com.scaler.productservice.dtos;

import com.scaler.productservice.models.Category;
import com.scaler.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {

    private long id;
    private String title;
    private Double price;
    private String category;
    private String description;
    private String image;

    public Product toProduct(){

        Product product = new Product();
        product.setId(getId()); // we can use getId() because of @Getter annotation or else direct use id that will also work because private long id; is in the same class
        product.setTitle(getTitle());
        product.setDescription(getDescription());
        product.setPrice(getPrice());
        product.setImageUrl(getImage());

        Category category = new Category();
        category.setName(getCategory());

        product.setCategory(category);

        return product;
    }

}
