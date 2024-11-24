package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesService {
    ProductSalesResponseDTO createProductSales(ProductSalesRequestDTO productSalesRequestDTO);
    List<ProductSalesResponseDTO> getAllProductSales();
    List<ProductSalesResponseDTO> getProductSalesByDate(LocalDate dateProductSales);
    List<ProductSalesResponseDTO> getProductSalesByPeriod(String period);
    List<ProductSalesResponseDTO> getProductSalesByProductCategories(String productCategories);
    ProductSalesResponseDTO updateProductSales(Long id, ProductSalesRequestDTO productSalesRequestDTO);
    ProductSalesResponseDTO deleteProductSales(Long id);
}
