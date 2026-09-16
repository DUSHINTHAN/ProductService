package com.scaler.productservice.services;

import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Category;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.CategoryRepository;
import com.scaler.productservice.repositories.ProductRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service("databaseProductService")
public class DatabaseProductService implements ProductService{

    private RedisTemplate<String, Product> redisTemplate;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public DatabaseProductService(ProductRepository productRepository, CategoryRepository categoryRepository, RedisTemplate<String, Product> redisTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductDetails(long id)throws ProductNotFoundException {

        Product productFromCache = redisTemplate.opsForValue().get(String.valueOf(id));

        if(productFromCache != null){

            return productFromCache;
        }

        Optional<Product> productOptionalFromDatabase = productRepository.findById(id);

        if(productOptionalFromDatabase.isEmpty()){

            throw new ProductNotFoundException("Product Not Found with id: " + id);
        }

        Product productFromDatabase = productOptionalFromDatabase.get();

        redisTemplate.opsForValue().set(String.valueOf(id), productFromDatabase, Duration.ofMinutes(10));

        return productFromDatabase;

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
