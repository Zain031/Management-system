package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Product name is required")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    @JsonProperty("product_name")
    private String productName;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be a positive number")
    @JsonProperty("product_price")
    private Long productPrice;

    @NotBlank(message = "Categories cannot be blank")
    @Pattern(regexp = "DRINKS/FOODS", message = "Categories must be FOODS or DRINKS")
    @JsonProperty("categories")
    private String categories;

    @JsonProperty("available_stock")
    private Boolean availableStock;

}
