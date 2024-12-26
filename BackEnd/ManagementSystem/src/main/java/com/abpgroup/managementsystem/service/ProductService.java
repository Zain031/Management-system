package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.model.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    ProductResponseDTO deleteProduct(Long id);
    Page<ProductResponseDTO> getAllProductsByAvailableStock(Pageable pageable, String availableStock);
    Page<ProductResponseDTO> getProductByCategory(String category, Pageable pageable);
    Page<ProductResponseDTO> getProductByProductName(String productName, Pageable pageable);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    byte[] generatedPdf(List<Products> products) ;
    List<ProductResponseDTO> getAllProducts();
}
