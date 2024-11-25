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
    @JsonProperty("id_user")
    private Long idUser;

    @JsonProperty("material_category")
    private String materialCategory;

    @JsonProperty("material_name")
    private String materialName;
    @JsonProperty("material_price_unit")
    private Long materialPriceUnit;
    @JsonProperty("material_quantity")
    private Long materialQuantity;
    @JsonProperty("material_discount")
    private Long materialDiscount;
    @JsonProperty("material_price_discount")
    private Long materialPriceDiscount;
    @JsonProperty("material_total_price")
    private Long materialTotalPrice;
    @JsonProperty("date_material_buy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMaterialBuy;
}
