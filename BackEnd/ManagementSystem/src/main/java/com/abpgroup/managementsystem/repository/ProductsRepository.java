package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    Page<Products> findAllByAvailableStock(Pageable sortedByPrice, Boolean availableStock);

    @Query("SELECT p FROM Products p WHERE p.idProduct = :attr0 AND p.availableStock = true")
    Optional<Products> findByIdAndAvailable(Long attr0);
}
