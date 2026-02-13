package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductDetails(long id) {
        // TODO Auto-generated method stub


        return null;
    }
    @Override
    public void getAllProducts() {
        // TODO Auto-generated method stub

    }

    @Override
    public void createProduct() {
        // TODO Auto-generated method stub

    }
}
