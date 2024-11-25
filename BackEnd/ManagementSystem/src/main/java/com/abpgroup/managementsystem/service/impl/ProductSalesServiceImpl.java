package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.mapper.ProductSalesMapper;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                .build();

        productSalesRepository.save(productSales);

        return convertToResponse(productSales);
    }

    @Override
    public List<ProductSalesResponseDTO> getAllProductSales() {
        return productSalesRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ProductSalesResponseDTO> getProductSalesByDate(LocalDate dateProductSales) {
        return ProductSalesMapper.toListProductSalesResponseDTO(productSalesRepository.findByDateProductSales(dateProductSales));
    }

    @Override
    public List<ProductSalesResponseDTO> getProductSalesByPeriod(String period) {
        return ProductSalesMapper.toListProductSalesResponseDTO(productSalesRepository.findByPeriod(period.toUpperCase()));
    }

    @Override
    public List<ProductSalesResponseDTO> getProductSalesByProductCategories(String productCategories) {
        return ProductSalesMapper.toListProductSalesResponseDTO(productSalesRepository.findByProduct_Categories(Products.ProductCategory.valueOf(productCategories.toUpperCase())));
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
