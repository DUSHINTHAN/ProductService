package com.scaler.productservice.services;

import com.scaler.productservice.exceptions.ProductNotFoundException;
import com.scaler.productservice.models.Category;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.CategoryRepository;
import com.scaler.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DatabaseProductServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final RedisTemplate<String, Product> redisTemplate = Mockito.mock(RedisTemplate.class);
    private final ValueOperations<String, Product> valueOperations = Mockito.mock(ValueOperations.class);

    private final DatabaseProductService databaseProductService =
            new DatabaseProductService(productRepository, categoryRepository, redisTemplate);

    // ---------- getProductDetails ----------

    @Test
    void getProductDetails_cacheHit_returnsFromCache_neverHitsDb() {
        Product cached = new Product();
        cached.setId(1L);
        cached.setTitle("Cached Product");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("1")).thenReturn(cached);

        Product result = databaseProductService.getProductDetails(1L);

        Assertions.assertEquals("Cached Product", result.getTitle());
        verify(productRepository, never()).findByIdAndIsDeletedFalse(anyLong());
        verify(valueOperations, never()).set(any(), any(), any());
    }

    @Test
    void getProductDetails_cacheMiss_hitsDbAndPopulatesCache() {
        Product fromDb = new Product();
        fromDb.setId(2L);
        fromDb.setTitle("DB Product");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("2")).thenReturn(null);
        when(productRepository.findByIdAndIsDeletedFalse(2L)).thenReturn(Optional.of(fromDb));

        Product result = databaseProductService.getProductDetails(2L);

        Assertions.assertEquals("DB Product", result.getTitle());
        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(valueOperations, times(1)).set(eq("2"), eq(fromDb), any());
    }

    @Test
    void getProductDetails_notInCacheOrDb_throwsProductNotFoundException() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("999")).thenReturn(null);
        when(productRepository.findByIdAndIsDeletedFalse(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> databaseProductService.getProductDetails(999L));

        verify(valueOperations, never()).set(any(), any(), any());
    }

    // ---------- getAllProducts ----------

    @Test
    void getAllProducts_delegatesToRepository() {

        Product p1 = new Product();
        Product p2 = new Product();

        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        productList.add(p2);

        when(productRepository.findAllByIsDeletedFalse())
                .thenReturn(productList);

        Assertions.assertEquals(2, databaseProductService.getAllProducts().size());
        verify(productRepository, times(1)).findAllByIsDeletedFalse();
    }

    // ---------- createProduct ----------

    @Test
    void createProduct_existingCategory_reusesIt() {

        Category electronics = new Category();
        electronics.setId(5L);
        electronics.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(electronics);
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = databaseProductService.createProduct(
                "Phone", "desc", "img.png", 999.0, "Electronics");

        Assertions.assertEquals("Phone", result.getTitle());
        Assertions.assertEquals(5L, result.getCategory().getId());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createProduct_newCategory_buildsAndAttachesIt() {

        when(categoryRepository.findByName("NewCat")).thenReturn(null);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        when(productRepository.save(productCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = databaseProductService.createProduct(
                "Gadget", "desc", "img.png", 49.0, "NewCat");

        Assertions.assertEquals("NewCat", result.getCategory().getName());
        Assertions.assertEquals("NewCat", productCaptor.getValue().getCategory().getName());
        // relies on @ManyToOne(cascade = PERSIST) on Product.category to persist
        // a brand-new category transitively when the product is saved
        verify(categoryRepository, never()).save(any());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ---------- updateProduct ----------

    @Test
    void updateProduct_existingProduct_updatesFieldsAndInvalidatesCache() {

        Product existing = new Product();
        existing.setId(3L);
        existing.setTitle("Old Title");

        when(productRepository.findByIdAndIsDeletedFalse(3L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findByName("Electronics")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = databaseProductService.updateProduct(3L, "New Title", "desc", "img.png", 199.0, "Electronics");

        Assertions.assertEquals("New Title", result.getTitle());
        Assertions.assertEquals("Electronics", result.getCategory().getName());
        verify(redisTemplate, times(1)).delete("3");
    }

    @Test
    void updateProduct_nonExistentProduct_throwsProductNotFoundException() {
        when(productRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> databaseProductService.updateProduct(99L, "x", "x", "x", 10.0, "x"));
    }

    // ---------- deleteProduct ----------

    @Test
    void deleteProduct_existingProduct_setsIsDeletedTrueAndInvalidatesCache() {

        Product existing = new Product();
        existing.setId(4L);

        when(productRepository.findByIdAndIsDeletedFalse(4L)).thenReturn(Optional.of(existing));

        databaseProductService.deleteProduct(4L);

        Assertions.assertTrue(existing.isDeleted);
        verify(productRepository, times(1)).save(existing);
        verify(redisTemplate, times(1)).delete("4");
    }

    @Test
    void deleteProduct_nonExistentProduct_throwsProductNotFoundException() {
        when(productRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> databaseProductService.deleteProduct(99L));
    }

}