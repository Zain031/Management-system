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
public class FoodSalesResponseDTO {
    @JsonProperty("id_food_sales")
    private Long idFoodSales;

    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;

    @JsonProperty("foods")
    private FoodsResponseDTO foodsResponseDTO;

    @JsonProperty("total_food_sales_price")
    private long totalFoodSalesPrice;

    @JsonProperty("date_food_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateFoodSales;

    @JsonProperty("period")
    private String period;

}
