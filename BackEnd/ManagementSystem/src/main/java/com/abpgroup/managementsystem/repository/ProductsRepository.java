package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findProductsByCategories(Products.ProductCategory categories);

    Page<Products> findProductsByCategories(Products.ProductCategory categories, Pageable pageable);

}
