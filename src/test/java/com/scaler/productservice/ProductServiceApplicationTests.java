//package com.scaler.productservice;
//
//import com.scaler.productservice.models.Category;
//import com.scaler.productservice.models.Product;
//import com.scaler.productservice.repositories.CategoryRepository;
//import com.scaler.productservice.repositories.ProductRepository;
//import com.scaler.productservice.repositories.projections.ProductTitleAndDescription;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//class ProductServiceApplicationTests {
//
//	@Autowired
//	ProductRepository productRepository;
//
//	@Autowired
//	CategoryRepository categoryRepository;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	public void test() {
//
//		Optional<Product> productOptional = productRepository.findByTitleAndCategory_Name("iPhone", "Electronics");
//
//		System.out.println(productOptional.get().getTitle());
//	}
//
//	@Test
//	public void test2() {
//
//		List<Product> productOptional = productRepository.getByCategoryName("Electronics");
//
//		System.out.println(productOptional.get(0).getTitle()); // this get is underlined beecause that we need to handle the case when the list is empty but for simplicity we are not handling that case here
//	}
//
//	@Test
//	public void test3() {
//
//		Product productDetail = productRepository.getProductDetail2(1);
//
//		System.out.print(productDetail.getTitle());
//
//	}
//
//	@Test
//	public void test4() {
//
//		ProductTitleAndDescription productDetail = productRepository.getProductDetail3(1);
//
//		System.out.print(productDetail.getTitle() +  "," + productDetail.getDescription());
//
//	}
//
//	@Test
//	@Transactional // we need to add transactional annotation here because we are trying to fetch the products of the category and the products are lazily loaded by default and if we do not add transactional annotation then we will get an error because the session will be closed before we try to fetch the products of the category
//	public void testFetchTypes(){
//
//		Optional<Category> category = categoryRepository.findById(1L);
//		System.out.print(category.get().getName());
//		System.out.println(category.get().getProducts());
//	}
//
//	@Test
//	@Transactional
//	public void nplus1ProblemTest(){
//
//		List<Category> categories = categoryRepository.findAll();
//
//		for(Category category : categories){
//			for(Product product : category.getProducts()){
//
//				System.out.println(product.getTitle());
//			}
//		}
//	}
//
//}
//
