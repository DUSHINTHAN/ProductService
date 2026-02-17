package com.scaler.productservice.controllers;

import com.scaler.productservice.dtos.CreateProductRequestDto;
import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.services.ProductService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){

        this.productService =  productService;
    }
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        // Logic to retrieve all products
           return productService.getAllProducts();
    }

    @GetMapping("/products/{Id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable("Id") long id) throws ProductNotFoundException {
        // Logic to retrieve product details by ID

        Product product =  productService.getProductDetails(id);

        ResponseEntity<Product> responseEntity = new ResponseEntity<>(product, HttpStatusCode.valueOf(201));

        return responseEntity;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        // Logic to create a new product

        Product product = productService.createProduct(
                createProductRequestDto.getTitle(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getImage(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getCategory()
        );

        ResponseEntity<Product> responseEntity = new ResponseEntity<>(product, HttpStatusCode.valueOf(201));

        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions() {
        // Logic to handle exceptions

    }
}
