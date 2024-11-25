package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSalesResponseDTO {
    @JsonProperty("id_product_sales")
    private Long idProductSales;

    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;

    @JsonProperty("product")
    private ProductResponseDTO productResponseDTO;

    @JsonProperty("total_product")
    private long totalProduct;

    @JsonProperty("leftover_product_sales")
    private long leftoverProductSales;

    @JsonProperty("total_product_sales_price")
    private long totalProductSalesPrice;

    @JsonProperty("date_product_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateProductSales;

    @JsonProperty("period")
    private String period;
}
