package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InventoryResponseDTO {
    @JsonProperty("id_material")
    private Long idMaterial;
    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;
    @JsonProperty("material_category")
    private String materialCategory;
    @JsonProperty("material_name")
    private String materialName;
    @JsonProperty("material_price_unit")
    private Double materialPriceUnit;
    @JsonProperty("material_quantity")
    private Double materialQuantity;
    @JsonProperty("material_discount")
    private Double materialDiscount;
    @JsonProperty("material_price_discount")
    private Double materialPriceDiscount;
    @JsonProperty("material_total_price")
    private Double materialTotalPrice;
    @JsonProperty("date_material_buy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMaterialBuy;
    @JsonProperty("period")
    private String period;
    @JsonProperty("years")
    private Long years;
}
