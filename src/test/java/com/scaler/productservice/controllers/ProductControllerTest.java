package com.scaler.productservice.controllers;

import com.scaler.productservice.dtos.CreateProductRequestDto;
import com.scaler.productservice.dtos.UpdateProductRequestDto;
import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


class ProductControllerTest {

    //direct constructor injection of the ProductService mock into the ProductController

    private ProductService productService = Mockito.mock(ProductService.class);

    private ProductController productController = new ProductController(productService);

    @Test
    void getAllProducts_returnsWhatServiceReturns() {

        Product p1 = new Product();
        p1.setTitle("abc");
        Product p2 = new Product();
        p2.setTitle("xyz");

        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        productList.add(p2);

        when(productService.getAllProducts()).thenReturn(productList);

        List<Product> products = productController.getAllProducts();

        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("abc", products.get(0).getTitle());
        Assertions.assertEquals("xyz", products.get(1).getTitle());
    }

    @Test
    void getProductDetails_found_returns201WithProduct() {

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");

        when(productService.getProductDetails(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductDetails(1L);

        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Test Product", response.getBody().getTitle());
    }

    @Test
    void getProductDetails_notFound_throwsException() {

        when(productService.getProductDetails(999L))
                .thenThrow(new ProductNotFoundException("Product Not Found with id: 999"));

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.getProductDetails(999L));
    }

    @Test
    void createProduct_returns201WithCreatedProduct() {

        CreateProductRequestDto dto = new CreateProductRequestDto();
        dto.setTitle("New");
        dto.setDescription("desc");
        dto.setImage("img.png");
        dto.setPrice(20.0);
        dto.setCategory("Books");

        Product created = new Product();
        created.setId(5L);
        created.setTitle("New");

        when(productService.createProduct("New", "desc", "img.png", 20.0, "Books"))
                .thenReturn(created);

        ResponseEntity<Product> response = productController.createProduct(dto);

        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals(5L, response.getBody().getId());
        verify(productService, times(1))
                .createProduct("New", "desc", "img.png", 20.0, "Books");
    }

    @Test
    void updateProduct_returns200WithUpdatedProduct() {

        UpdateProductRequestDto dto = new UpdateProductRequestDto();
        dto.setTitle("Updated");
        dto.setDescription("desc");
        dto.setImage("img.png");
        dto.setPrice(50.0);
        dto.setCategory("Books");

        Product updated = new Product();
        updated.setId(1L);
        updated.setTitle("Updated");

        when(productService.updateProduct(1L, "Updated", "desc", "img.png", 50.0, "Books"))
                .thenReturn(updated);

        ResponseEntity<Product> response = productController.updateProduct(1L, dto);

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Updated", response.getBody().getTitle());
    }

    @Test
    void updateProduct_notFound_throwsException() {

        UpdateProductRequestDto dto = new UpdateProductRequestDto();
        dto.setTitle("Updated");

        when(productService.updateProduct(anyLong(), any(), any(), any(), anyDouble(), any()))
                .thenThrow(new ProductNotFoundException("Product Not Found with id: 999"));

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.updateProduct(999L, dto));
    }

    @Test
    void deleteProduct_returns204() {

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        Assertions.assertEquals(204, response.getStatusCode().value());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void deleteProduct_notFound_throwsException() {

        //because deleteProduct method in ProductService is void method we cannot use when().thenReturn() instead we need to use doThrow().when() to mock the exception thrown by the void method
        doThrow(new ProductNotFoundException("Product Not Found with id: 999"))
                .when(productService).deleteProduct(999L);

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productController.deleteProduct(999L));
    }
}