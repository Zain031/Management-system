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
public class DrinkSalesRequestDTO {

    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Drink ID is required")
    @JsonProperty("id_drink")
    private Long idDrink;

    @NotNull(message = "Total drink sales is required")
    @JsonProperty("total_drink_sales")
    private Long totalDrinkSales;

    @NotNull(message = "Leftover drink sales is required")
    @JsonProperty("leftover_drink_sales")
    private Long leftoverDrinkSales;

    @NotNull(message = "Date drink sales is required")
    @JsonProperty("date_drink_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateDrinkSales;
}
