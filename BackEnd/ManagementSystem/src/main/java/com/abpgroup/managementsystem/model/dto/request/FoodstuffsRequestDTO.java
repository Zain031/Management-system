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
public class FoodstuffsRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Foodstuff name is required")
    @Size(min = 1, max = 100, message = "Foodstuff name must be between 1 and 100 characters")
    @JsonProperty("foodstuff_name")
    private String foodstuffName;

    @NotNull(message = "Foodstuff price unit is required")
    @Positive(message = "Foodstuff price unit must be a positive number")
    @JsonProperty("foodstuff_price_unit")
    private Long foodstuffPriceUnit;

    @NotNull(message = "Foodstuff quantity is required")
    @Positive(message = "Foodstuff quantity must be a positive number")
    @JsonProperty("foodstuff_quantity")
    private Long foodstuffQuantity;

    @NotNull(message = "Foodstuff discount is required")
    @Positive(message = "Foodstuff discount must be a positive number")
    @JsonProperty("foodstuff_discount")
    private Long foodstuffDiscount;

    @NotNull(message = "Date of foodstuff purchase is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_foodstuff_buy")
    private LocalDate dateFoodstuffBuy;
}
