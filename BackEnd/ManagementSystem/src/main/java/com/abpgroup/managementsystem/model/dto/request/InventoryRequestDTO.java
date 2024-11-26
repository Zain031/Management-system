package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Material name is required")
    @Size(min = 1, max = 100, message = "Material name must be between 1 and 100 characters")
    @JsonProperty("material_name")
    private String materialName;

    @NotNull(message = "Material category is required")
    @Size(min = 1, max = 100, message = "Material category must be between 1 and 100 characters")
    @JsonProperty("material_category")
    private String materialCategory;

    @NotNull(message = "Material price unit is required")
    @Positive(message = "Material price unit must be a positive number")
    @JsonProperty("material_price_unit")
    private Long materialPriceUnit;

    @NotNull(message = "Material quantity is required")
    @Positive(message = "Material quantity must be a positive number")
    @JsonProperty("material_quantity")
    private Long materialQuantity;

    @NotNull(message = "Material discount is required")
    @Positive(message = "Material discount must be a positive number")
    @JsonProperty("material_discount")
    private Long materialDiscount;

    @NotNull(message = "Date of material purchase is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_material_buy")
    private LocalDate dateMaterialBuy;
}
