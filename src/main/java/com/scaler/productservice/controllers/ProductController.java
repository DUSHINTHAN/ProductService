package com.scaler.productservice.controllers;

import com.scaler.productservice.models.Product;
import com.scaler.productservice.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){

        this.productService =  productService;
    }
    @GetMapping("/products")
    public void getAllProducts() {
        // Logic to retrieve all products
    }

    @GetMapping("/products/{Id}")
    public Product getProductDetails(@PathVariable("Id") long id) {
        // Logic to retrieve product details by ID

        return productService.getProductDetails(id);

    }

    public void createProduct() {
        // Logic to create a new product
    }


}
