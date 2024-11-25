package com.abpgroup.managementsystem.mapper;

import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductSalesMapper {
    public static List<ProductSalesResponseDTO> toListProductSalesResponseDTO(List<ProductSales> productSales) {
        List<ProductSalesResponseDTO> result = new ArrayList<>();

        for (ProductSales data : productSales) {
            UsersResponseDTO user = UsersResponseDTO.builder()
                    .idUser(data.getUser().getIdUser())
                    .email(data.getUser().getEmail())
                    .name(data.getUser().getName())
                    .role(data.getUser().getRole().name())
                    .build();

            UsersResponseDTO productCreator = UsersResponseDTO.builder()
                    .idUser(data.getProduct().getUser().getIdUser())
                    .email(data.getProduct().getUser().getEmail())
                    .name(data.getProduct().getUser().getName())
                    .role(data.getProduct().getUser().getRole().name())
                    .build();

            ProductResponseDTO product = ProductResponseDTO.builder()
                    .idProduct(data.getProduct().getIdProduct())
                    .productName(data.getProduct().getProductName())
                    .productPrice(data.getProduct().getProductPrice())
                    .usersResponseDTO(productCreator)
                    .build();

            result.add(ProductSalesResponseDTO.builder()
                    .idProductSales(data.getIdProductSales())
                    .totalProductSalesPrice(data.getTotalProductSales())
                    .dateProductSales(data.getDateProductSales())
                    .period(data.getPeriod())
                    .usersResponseDTO(user)
                    .productResponseDTO(product)
                    .build());
        }

        return result;
    }
}
