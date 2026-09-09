package com.scaler.productservice.repositories;

import com.scaler.productservice.models.Product;
import com.scaler.productservice.repositories.projections.ProductTitleAndDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

    Optional<Product> findById(long id);

    List<Product> findAll();

    Optional<Product> findByTitleAndCategory_Name(String title, String categoryName);

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> getByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "SELECT * FROM product p JOIN category c ON p.category_id = c.id WHERE c.name = :categoryName", nativeQuery = true)
    List<Product> getByCategoryNameNative(@Param("categoryName") String categoryName); //(same as above but using native query)

    @Query(value = "SELECT * FROM product WHERE id = :id", nativeQuery = true)
    Product getProductDetail2(@Param("id") long id); // (same as findById but using native query)
                                                     //we could have return type as Optional<Product> but for simplicity we have kept it product only as return type and we are not handling the case
                                                     // when the product is not found in the database but in real world application we should handle that case as well

    @Query(value = "SELECT title, description FROM product WHERE id = :id", nativeQuery = true)
    ProductTitleAndDescription getProductDetail3(@Param("id") long id);

    Page<Product> findByTitleContaining(String query, Pageable pageable);
}
