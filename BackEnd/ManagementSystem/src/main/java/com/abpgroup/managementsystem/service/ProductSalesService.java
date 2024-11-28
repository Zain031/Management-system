package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesService {
    ProductSalesResponseDTO createProductSales(ProductSalesRequestDTO productSalesRequestDTO);
    Page <ProductSalesResponseDTO> getAllProductSales(Pageable pageable);
    Page<ProductSalesResponseDTO> getProductSalesByDate(LocalDate dateProductSales, Pageable pageable);
    Page<ProductSalesResponseDTO> getProductSalesByPeriodAndYears(String period, Long years, Pageable pageable);
    Page<ProductSalesResponseDTO> getProductSalesByProductCategories(String productCategories, Pageable pageable);
    ProductSalesResponseDTO updateProductSales(Long id, ProductSalesRequestDTO productSalesRequestDTO);
    ProductSalesResponseDTO deleteProductSales(Long id);
    Page<ProductSalesResponseDTO> getProductSalesByProductName(String productName, Pageable pageable);
    byte[] generatedPdf(List<ProductSales> productSalesList);
}
