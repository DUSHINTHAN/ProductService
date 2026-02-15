package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class DatabaseProductService implements ProductService{

    @Override
    public Product getProductDetails(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product createProduct(String title, String description, String image, double price , String category) {
        // TODO Auto-generated method stub
        return null;
    }
}
