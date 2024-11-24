package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getProductByCategory(String category);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    ProductResponseDTO deleteProduct(Long id);
}
