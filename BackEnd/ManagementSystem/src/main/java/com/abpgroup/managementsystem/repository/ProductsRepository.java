package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Page<Products> findProductsByCategories(Products.ProductCategory categories, Pageable pageable);

    Page<Products> findProductsByCategoriesOrderByProductPriceAsc(Products.ProductCategory categories, Pageable pageable);
}
