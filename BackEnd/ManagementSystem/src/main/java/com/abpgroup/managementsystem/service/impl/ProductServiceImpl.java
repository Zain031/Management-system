package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Products;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.ProductsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productRepository;
    private final UsersRepository usersRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO.getProductName() == null || productRequestDTO.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (productRequestDTO.getProductPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        if (productRequestDTO.getCategories() == null || productRequestDTO.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be empty");
        }

        Users user = usersRepository.findById(productRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Products product = Products.builder()
                .user(user)
                .productName(productRequestDTO.getProductName())
                .productPrice(productRequestDTO.getProductPrice())
                .categories(Products.ProductCategory.valueOf(productRequestDTO.getCategories().toUpperCase()))
                .build();

        productRepository.save(product);

        return convertToResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToResponse).orElse(null);
    }

    @Override
    public List<ProductResponseDTO> getProductByCategory(String category) {
        return productRepository.findProductsByCategories(Products.ProductCategory.valueOf(category.toUpperCase())).stream().map(this::convertToResponse).toList();
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Users user = usersRepository.findById(productRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        product.setUser(user);
        product.setProductName(productRequestDTO.getProductName());
        product.setProductPrice(productRequestDTO.getProductPrice());
        product.setCategories(Products.ProductCategory.valueOf(productRequestDTO.getCategories().toUpperCase()));
        productRepository.save(product);
        return convertToResponse(product);
    }

    @Override
    public ProductResponseDTO deleteProduct(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(product);
        return convertToResponse(product);
    }

    private ProductResponseDTO convertToResponse(Products product) {
        return ProductResponseDTO.builder()
                .idProduct(product.getIdProduct())
                .usersResponseDTO(convertToUsersResponseDTO(product.getUser()))
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .categories(product.getCategories().name())
                .build();
    }

    private UsersResponseDTO convertToUsersResponseDTO(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
