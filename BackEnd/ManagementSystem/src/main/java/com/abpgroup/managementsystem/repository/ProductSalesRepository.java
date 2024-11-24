package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {
    List<ProductSales> findProductSalesByDateProductSales(LocalDate dateProductSales);

    List<ProductSales> findProductSalesByPeriod(String period);

    List<ProductSales> findProductSalesByProduct_Categories(Products.ProductCategory productCategories);

    List<ProductSales> findByDateProductSales(LocalDate dateProductSales);

    List<ProductSales> findByPeriod(String period);

    List<ProductSales> findByProduct_Categories(Products.ProductCategory productCategories);
}
