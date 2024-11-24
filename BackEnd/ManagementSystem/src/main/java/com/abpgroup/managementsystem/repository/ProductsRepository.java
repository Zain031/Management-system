package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findProductsByCategories(Products.ProductCategory categories);
}
