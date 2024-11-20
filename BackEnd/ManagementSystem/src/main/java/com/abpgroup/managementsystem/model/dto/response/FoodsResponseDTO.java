package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodsResponseDTO {
    @JsonProperty("id_food")
    private Long idFood;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("food_name")
    private String foodName;
    @JsonProperty("food_price")
    private Long foodPrice;
    @JsonProperty("total_food")
    private Long totalFood;
    @JsonProperty("total_food_price")
    private Long totalFoodPrice;
}
