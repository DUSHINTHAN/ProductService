package com.scaler.productservice;

import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.ProductRepository;
import com.scaler.productservice.repositories.projections.ProductTitleAndDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void test() {

		Optional<Product> productOptional = productRepository.findByTitleAndCategory_Name("iPhone", "Electronics");

		System.out.println(productOptional.get().getTitle());
	}

	@Test
	public void test2() {

		List<Product> productOptional = productRepository.getByCategoryName("Electronics");

		System.out.println(productOptional.get(0).getTitle()); // this get is underlined beecause that we need to handle the case when the list is empty but for simplicity we are not handling that case here
	}

	@Test
	public void test3() {

		Product productDetail = productRepository.getProductDetail2(1);

		System.out.print(productDetail.getTitle());

	}

	@Test
	public void test4() {

		ProductTitleAndDescription productDetail = productRepository.getProductDetail3(1);

		System.out.print(productDetail.getTitle() +  "," + productDetail.getDescription());

	}
}

