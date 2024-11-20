package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinksRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Drink name is required")
    @Size(min = 1, max = 100, message = "Drink name must be between 1 and 100 characters")
    @JsonProperty("drink_name")
    private String drinkName;

    @NotNull(message = "Drink price is required")
    @Positive(message = "Drink price must be a positive number")
    @JsonProperty("drink_price")
    private Long drinkPrice;

    @NotNull(message = "Total drink is required")
    @Positive(message = "Total drink must be a positive number")
    @JsonProperty("total_drink")
    private Long totalDrink;
}
