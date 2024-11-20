package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinksResponseDTO {
    @JsonProperty("id_drink")
    private Long idDrink;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("drink_name")
    private String drinkName;
    @JsonProperty("drink_price")
    private Long drinkPrice;
    @JsonProperty("total_drink")
    private Long totalDrink;
    @JsonProperty("total_drink_price")
    private Long totalDrinkPrice;
}
