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
    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;
    @JsonProperty("food_name")
    private String foodName;
    @JsonProperty("food_price")
    private Long foodPrice;
}
