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
public class FoodstuffsResponseDTO {
    @JsonProperty("id_foodstuff")
    private Long idFoodstuff;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("foodstuff_name")
    private String foodstuffName;
    @JsonProperty("foodstuff_price_unit")
    private Long foodstuffPriceUnit;
    @JsonProperty("foodstuff_quantity")
    private Long foodstuffQuantity;
    @JsonProperty("foodstuff_discount")
    private Long foodstuffDiscount;
    @JsonProperty("foodstuff_price_discount")
    private Long foodstuffPriceDiscount;
    @JsonProperty("foodstuff_total_price")
    private Long foodstuffTotalPrice;
    @JsonProperty("date_foodstuff_buy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFoodstuffBuy;
}
