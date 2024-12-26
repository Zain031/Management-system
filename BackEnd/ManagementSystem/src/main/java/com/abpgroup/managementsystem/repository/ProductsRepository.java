package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Page<Products> findProductsByCategories(Products.ProductCategory categories, Pageable pageable);

    @Query(
            "SELECT p FROM Products p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))"
    )
    Page<Products> getProductsByProductName(@Param("productName") String productName, Pageable sortedByPrice);

    @Query(
            "SELECT p FROM Products p WHERE p.availableStock = :availableStock"
    )
    Page<Products> findAllByAvailableStock(Pageable sortedByPrice,@Param("availableStock") Products.AvailableStock availableStock);

    @Query("SELECT p FROM Products p WHERE p.availableStock = :availableStock")
    List<Products> findAllAvailableStock(Products.AvailableStock availableStock);
}
