package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;

public interface ProductService {

        public void getAllProducts();
        public Product getProductDetails(long id);
        public void createProduct();
}
