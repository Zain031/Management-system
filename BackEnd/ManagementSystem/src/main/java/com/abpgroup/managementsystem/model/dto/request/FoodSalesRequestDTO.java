package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodSalesRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Food ID is required")
    @JsonProperty("id_food")
    private Long idFood;

    @NotNull(message = "Total food sales is required")
    @JsonProperty("total_food_sales")
    private Long totalFoodSales;

    @NotNull(message = "Leftover food sales is required")
    @JsonProperty("leftover_food_sales")
    private Long leftoverFoodSales;

    @NotNull(message = "Date food sales is required")
    @JsonProperty("date_food_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateFoodSales;

}
