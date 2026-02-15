package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;

import java.util.List;

public interface ProductService {

        public List<Product> getAllProducts();
        public Product getProductDetails(long id);
        public Product createProduct(String title, String description, String image, double price , String category);
}
