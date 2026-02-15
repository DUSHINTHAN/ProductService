package com.scaler.productservice.services;

import com.scaler.productservice.dtos.FakeStoreCreateProductDto;
import com.scaler.productservice.dtos.FakeStoreProductDto;
import com.scaler.productservice.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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


        return responseEntity.getBody().toProduct();


    }
    @Override
    public List<Product> getAllProducts(){
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

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
}
