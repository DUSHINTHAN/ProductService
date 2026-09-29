package com.scaler.productservice.services;

import com.scaler.productservice.dtos.FakeStoreProductDto;
import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakeStoreProductServiceTest {

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private RedisTemplate<String, Product> redisTemplate = Mockito.mock(RedisTemplate.class);
    private ValueOperations<String, Product> valueOperations = Mockito.mock(ValueOperations.class);
    private FakeStoreProductService fakeStoreProductService =
            new FakeStoreProductService(restTemplate, redisTemplate);

    @Test
    void getProductDetails_cacheHit_returnsFromCache_neverCallsApi() {
        Product cached = new Product();
        cached.setId(1L);
        cached.setTitle("Cached iPhone");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("1")).thenReturn(cached);

        Product result = fakeStoreProductService.getProductDetails(1L);

        Assertions.assertEquals("Cached iPhone", result.getTitle());
        verify(restTemplate, never()).getForEntity(anyString(), eq(FakeStoreProductDto.class));
    }

    @Test
    void getProductDetails_cacheMiss_callsApiAndPopulatesCache() {
        FakeStoreProductDto dto = new FakeStoreProductDto();
        dto.setId(1L);
        dto.setTitle("iPhone");
        dto.setPrice(999.0);
        dto.setCategory("Electronics");

        ResponseEntity<FakeStoreProductDto> response =
                new ResponseEntity<>(dto, HttpStatusCode.valueOf(200));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("1")).thenReturn(null);
        when(restTemplate.getForEntity("https://fakestoreapi.com/products/1", FakeStoreProductDto.class))
                .thenReturn(response);

        Product product = fakeStoreProductService.getProductDetails(1L);

        Assertions.assertEquals("iPhone", product.getTitle());
        Assertions.assertEquals("Electronics", product.getCategory().getName());
        verify(valueOperations, times(1)).set(eq("1"), any(Product.class), any());
    }

    @Test
    void getProductDetails_whenApiReturnsEmptyBody_throwsProductNotFoundException() {
        ResponseEntity<FakeStoreProductDto> response =
                new ResponseEntity<>(null, HttpStatusCode.valueOf(200));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("404")).thenReturn(null);
        when(restTemplate.getForEntity("https://fakestoreapi.com/products/404", FakeStoreProductDto.class))
                .thenReturn(response);

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> fakeStoreProductService.getProductDetails(404L));

        verify(valueOperations, never()).set(any(), any(), any());
    }

    @Test
    void getAllProducts_mapsEveryDtoInArray() {
        FakeStoreProductDto d1 = new FakeStoreProductDto();
        d1.setTitle("A");
        d1.setPrice(10.0);
        FakeStoreProductDto d2 = new FakeStoreProductDto();
        d2.setTitle("B");
        d2.setPrice(20.0);

        when(restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDto[].class))
                .thenReturn(new FakeStoreProductDto[]{d1, d2});

        Assertions.assertEquals(2, fakeStoreProductService.getAllProducts().size());
    }

    @Test
    void createProduct_postsPayloadAndMapsResponse() {
        FakeStoreProductDto responseDto = new FakeStoreProductDto();
        responseDto.setTitle("New Product");
        responseDto.setPrice(10.0);

        when(restTemplate.postForObject(
                eq("https://fakestoreapi.com/products"), any(), eq(FakeStoreProductDto.class)))
                .thenReturn(responseDto);

        Product product = fakeStoreProductService.createProduct(
                "New Product", "desc", "img.png", 10.0, "Misc");

        Assertions.assertEquals("New Product", product.getTitle());
    }

    @Test
    void updateProduct_apiReturnsUpdated_mapsToProduct() {
        FakeStoreProductDto responseDto = new FakeStoreProductDto();
        responseDto.setTitle("Updated Title");
        responseDto.setPrice(10.0);

        ResponseEntity<FakeStoreProductDto> response =
                new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(200));

        when(restTemplate.exchange(
                eq("https://fakestoreapi.com/products/1"),
                eq(HttpMethod.PUT),
                any(),
                eq(FakeStoreProductDto.class)))
                .thenReturn(response);

        Product product = fakeStoreProductService.updateProduct(1L, "Updated Title", "d", "i", 10.0, "c");

        Assertions.assertEquals("Updated Title", product.getTitle());
        verify(redisTemplate, times(1)).delete("1");
    }

    @Test
    void updateProduct_apiReturnsEmptyBody_throwsProductNotFoundException() {
        ResponseEntity<FakeStoreProductDto> response =
                new ResponseEntity<>(null, HttpStatusCode.valueOf(200));

        when(restTemplate.exchange(
                eq("https://fakestoreapi.com/products/404"),
                eq(HttpMethod.PUT),
                any(),
                eq(FakeStoreProductDto.class)))
                .thenReturn(response);

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> fakeStoreProductService.updateProduct(404L, "x", "x", "x", 1.0, "x"));
    }

    @Test
    void deleteProduct_callsRestTemplateDeleteAndInvalidatesCache() {
        fakeStoreProductService.deleteProduct(1L);

        verify(restTemplate, times(1)).delete("https://fakestoreapi.com/products/1");
        verify(redisTemplate, times(1)).delete("1");
    }

}