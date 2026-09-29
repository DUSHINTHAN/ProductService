package com.scaler.productservice.services;

import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private SearchService searchService = new SearchService(productRepository);

    @Test
    void searchProducts_delegatesWithCorrectPageableAndSort() {

        Product fakeProduct = new Product();
        fakeProduct.setTitle("phone case");

        List<Product> productList = new ArrayList<>();
        productList.add(fakeProduct);

        Page<Product> fakePage = new PageImpl<>(productList);

        when(productRepository.findByTitleContaining(eq("phone"), any(Pageable.class)))
                .thenReturn(fakePage);

        Page<Product> result = searchService.searchProducts("phone", 0, 10);

        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchProducts_noMatches_returnsEmptyPage() {

        List<Product> emptyList = new ArrayList<>();

        Page<Product> emptyPage = new PageImpl<>(emptyList);

        when(productRepository.findByTitleContaining(eq("zzz"), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<Product> result = searchService.searchProducts("zzz", 0, 10);

        Assertions.assertEquals(0, result.getTotalElements());
    }
}