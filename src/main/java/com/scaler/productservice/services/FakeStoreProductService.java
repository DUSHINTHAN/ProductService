package com.scaler.productservice.services;

import com.scaler.productservice.dtos.FakeStoreCreateProductDto;
import com.scaler.productservice.dtos.FakeStoreProductDto;
import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;
    private RedisTemplate<String, Product> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Product> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductDetails(long id) {

        Product productFromCache = redisTemplate.opsForValue().get(String.valueOf(id));

        if(productFromCache != null){

            return productFromCache;
        }

 //       FakeStoreProductDto responseDto =
 //               restTemplate.getForObject(
 //                       "https://fakestoreapi.com/products/" + id,
 //                           FakeStoreProductDto.class
 //               );

        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.getForEntity(
                        "https://fakestoreapi.com/products/" + id,
                        FakeStoreProductDto.class
                );

 //       if(responseEntity.getStatusCode() == HttpStatusCode.valueOf(404)){
            //show some error message or throw some exception
 //           return null;
 //       }
 //       else if(responseEntity.getStatusCode() == HttpStatusCode.valueOf(500)){
            //show some error message or throw some exception
  //          return null;
  //      }
        FakeStoreProductDto responseBody = responseEntity.getBody();
        if(responseBody == null){

            throw new ProductNotFoundException("Product Not Found with id: " + id);
        }

        Product productFromResponse = responseBody.toProduct();

        redisTemplate.opsForValue().set(String.valueOf(id), productFromResponse, Duration.ofMinutes(10));

        return productFromResponse;

    }

    @Override
    public List<Product> getAllProducts(){

        FakeStoreProductDto[] responseDto =
                restTemplate.getForObject(
                        "https://fakestoreapi.com/products",
                        FakeStoreProductDto[].class
                );

        List<Product> products = new ArrayList<>();

        for(FakeStoreProductDto dto : responseDto){

            products.add(dto.toProduct());
        }

        return products;
    }

    @Override
    public Product createProduct(String title, String description, String image, double price , String category) {

        FakeStoreCreateProductDto requestDto = new FakeStoreCreateProductDto();

        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setImage(image);
        requestDto.setPrice(price);
        requestDto.setCategory(category);

        FakeStoreProductDto responseDto =
                restTemplate.postForObject(
                        "https://fakestoreapi.com/products",
                        requestDto,
                        FakeStoreProductDto.class
                );

        return responseDto.toProduct();
    }

    @Override
    public  Product updateProduct(long id, String title, String description, String image, double price, String category) {

        FakeStoreCreateProductDto requestDto = new FakeStoreCreateProductDto();

        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setImage(image);
        requestDto.setPrice(price);
        requestDto.setCategory(category);

        HttpEntity<FakeStoreCreateProductDto> requestEntity = new HttpEntity<>(requestDto);

        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.exchange(
                        "https://fakestoreapi.com/products/" + id,
                        HttpMethod.PUT,
                        requestEntity,
                        FakeStoreProductDto.class
                );
        FakeStoreProductDto responseBody = responseEntity.getBody();

        if(responseBody == null){

            throw new ProductNotFoundException("Product Not Found with id: " + id);
        }

        // Invalidate the cache for the updated product
        redisTemplate.delete(String.valueOf(id));

        return responseBody.toProduct();
    }

    public void deleteProduct(long id) {

        restTemplate.delete("https://fakestoreapi.com/products/" + id);

        // Invalidate the cache for the deleted product
        redisTemplate.delete(String.valueOf(id));
    }
}
