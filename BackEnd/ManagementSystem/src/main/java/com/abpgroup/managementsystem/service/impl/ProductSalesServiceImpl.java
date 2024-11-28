package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.entity.Products;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.ProductSalesRepository;
import com.abpgroup.managementsystem.repository.ProductsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.ProductSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductSalesServiceImpl implements ProductSalesService {
    private final ProductSalesRepository productSalesRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    @Override
    public ProductSalesResponseDTO createProductSales(ProductSalesRequestDTO productSalesRequestDTO) {
        Users user = usersRepository.findById(productSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Products product = productsRepository.findById(productSalesRequestDTO.getIdProduct())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        LocalDate dateProductSales = productSalesRequestDTO.getDateProductSales();

        if (productSalesRequestDTO.getTotalProductSales() <= 0) {
            throw new IllegalArgumentException("Total product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getLeftoverProductSales() <= 0) {
            throw new IllegalArgumentException("Leftover product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getDateProductSales() == null) {
            throw new IllegalArgumentException("Date product sales cannot be empty");
        }

        ProductSales productSales = ProductSales.builder()
                .user(user)
                .product(product)
                .totalProductSales(productSalesRequestDTO.getTotalProductSales())
                .leftoverProductSales(productSalesRequestDTO.getLeftoverProductSales())
                .totalLeftoverProductSalesPrice((productSalesRequestDTO.getLeftoverProductSales() * product.getProductPrice()))
                .totalProductSalesPrice((productSalesRequestDTO.getTotalProductSales() * product.getProductPrice())-(productSalesRequestDTO.getLeftoverProductSales() * product.getProductPrice()))
                .dateProductSales(productSalesRequestDTO.getDateProductSales())
                .period(dateProductSales.getMonth().name())
                .years(Long.valueOf(dateProductSales.getYear()))
                .build();

        productSalesRepository.save(productSales);

        return convertToResponse(productSales);
    }

    @Override
    public Page<ProductSalesResponseDTO> getAllProductSales(Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        return productSalesRepository.findAll(sortedByDateProductSales)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByDate(LocalDate dateProductSales, Pageable pageable) {
       Page<ProductSales> productSales = productSalesRepository.findProductSalesByDateProductSales(dateProductSales, pageable);
       return productSales.map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByPeriodAndYears(String period,Long years, Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByPeriodAndYears(period.toUpperCase(),years, sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByProductCategories(String productCategories, Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByProductCategories(productCategories.toUpperCase(), sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }

    @Override
    public ProductSalesResponseDTO updateProductSales(Long id, ProductSalesRequestDTO productSalesRequestDTO) {
        ProductSales productSales = productSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product sales not found"));
        Users user = usersRepository.findById(productSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Products product = productsRepository.findById(productSalesRequestDTO.getIdProduct())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (productSalesRequestDTO.getTotalProductSales() <= 0) {
            throw new IllegalArgumentException("Total product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getLeftoverProductSales() <= 0) {
            throw new IllegalArgumentException("Leftover product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getDateProductSales() == null) {
            throw new IllegalArgumentException("Date product sales cannot be empty");
        }

        LocalDate dateProductSales = productSalesRequestDTO.getDateProductSales();
        ProductSales updatedProductSales = ProductSales.builder()
                .idProductSales(productSales.getIdProductSales())
                .user(user)
                .product(product)
                .totalProductSales(productSalesRequestDTO.getTotalProductSales())
                .leftoverProductSales(productSalesRequestDTO.getLeftoverProductSales())
                .dateProductSales(dateProductSales)
                .period(dateProductSales.getMonth().name())
                .years(Long.valueOf(dateProductSales.getYear()))
                .build();
        productSalesRepository.save(updatedProductSales);
        return convertToResponse(updatedProductSales);
    }

    @Override
    public ProductSalesResponseDTO deleteProductSales(Long id) {
        ProductSales productSales = productSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product sales not found"));
        productSalesRepository.delete(productSales);
        return convertToResponse(productSales);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByProductName(String productName, Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByProductName(productName, sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }

    private ProductSalesResponseDTO convertToResponse(ProductSales productSales) {
        return ProductSalesResponseDTO.builder()
                .idProductSales(productSales.getIdProductSales())
                .usersResponseDTO(convertToUsersResponseDTO(productSales.getUser()))
                .productResponseDTO(convertToResponse(productSales.getProduct()))
                .totalProduct(productSales.getTotalProductSales())
                .leftoverProductSales(productSales.getLeftoverProductSales())
                .totalLeftoverProductSalesPrice(productSales.getTotalLeftoverProductSalesPrice())
                .totalProductSalesPrice(productSales.getTotalProductSalesPrice())
                .dateProductSales(productSales.getDateProductSales())
                .period(productSales.getPeriod())
                .years(productSales.getYears())
                .build();
    }
    private ProductResponseDTO convertToResponse(Products product) {
        return ProductResponseDTO.builder()
                .idProduct(product.getIdProduct())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .categories(product.getCategories().name())
                .usersResponseDTO(UsersResponseDTO.builder()
                        .idUser(product.getUser().getIdUser())
                        .email(product.getUser().getEmail())
                        .name(product.getUser().getName())
                        .role(product.getUser().getRole().name())
                        .build())
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
