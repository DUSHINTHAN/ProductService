package com.scaler.productservice.services;

import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Category;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.CategoryRepository;
import com.scaler.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service("databaseProductService")
public class DatabaseProductService implements ProductService{

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public DatabaseProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductDetails(long id)throws ProductNotFoundException {
        Optional<Product> optionalFromDatabase = productRepository.findById(id);
        if(optionalFromDatabase.isEmpty()){
            throw new ProductNotFoundException("Product Not Found with id: " + id);
        }
        else{
            return optionalFromDatabase.get();
        }

    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String description, String image, double price , String categoryName) {
        // TODO Auto-generated method stub
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(image);
        product.setPrice(price);

        Category categoryFromDatabase = categoryRepository.findByName(categoryName);

        if(categoryFromDatabase == null){
            Category newcategory = new Category();
            newcategory.setName(categoryName);

            categoryFromDatabase = newcategory;

           // categoryFromDatabase = categoryRepository.save(newcategory);
        }

        product.setCategory(categoryFromDatabase);


        return productRepository.save(product);
    }
}
