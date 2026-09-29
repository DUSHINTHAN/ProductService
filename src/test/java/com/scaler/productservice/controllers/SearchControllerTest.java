package com.scaler.productservice.controllers;

import com.scaler.productservice.dtos.SearchRequestDto;
import com.scaler.productservice.models.Product;
import com.scaler.productservice.services.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class SearchControllerTest {

    private SearchService searchService = Mockito.mock(SearchService.class);

    private SearchController searchController = new SearchController(searchService);

    @Test
    void searchProducts_passesQueryParamsThroughAndReturnsPage() {
        SearchRequestDto request = new SearchRequestDto();
        request.setQuery("shoe");
        request.setPageNumber(0);
        request.setPageSize(5);

        Product fakeProduct = new Product();
        fakeProduct.setId(1L);
        fakeProduct.setTitle("Running Shoe");

        List<Product> productList = new ArrayList<>();
        productList.add(fakeProduct);

        Page<Product> fakePage = new PageImpl<>(productList);

        when(searchService.searchProducts("shoe", 0, 5)).thenReturn(fakePage);

        Page<Product> result = searchController.searchProducts(request);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Running Shoe", result.getContent().get(0).getTitle());
        verify(searchService, times(1)).searchProducts("shoe", 0, 5);
    }

    @Test
    void searchProducts_noResults_returnsEmptyPage() {

        SearchRequestDto request = new SearchRequestDto();
        request.setQuery("notfound");
        request.setPageNumber(0);
        request.setPageSize(10);

        List<Product> emptyList = new ArrayList<>();
        Page<Product> emptyPage = new PageImpl<>(emptyList);

        when(searchService.searchProducts("notfound", 0, 10))
                .thenReturn(emptyPage);

        Page<Product> result = searchController.searchProducts(request);

        Assertions.assertEquals(0, result.getTotalElements());
        Assertions.assertTrue(result.isEmpty());
    }
}