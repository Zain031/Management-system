package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSalesRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Product ID is required")
    @JsonProperty("id_product")
    private Long idProduct;

    @NotNull(message = "Total product to sell is required")
    @JsonProperty("total_product_to_sell")
    private Long totalProductToSell;

    @NotNull(message = "Leftover product sales is required")
    @JsonProperty("leftover_product_sales")
    private Long leftoverProductSales;

    @NotNull(message = "Date product sales is required")
    @JsonProperty("date_product_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateProductSales;
}
