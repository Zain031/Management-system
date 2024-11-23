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
public class DrinkSalesResponseDTO {
    @JsonProperty ("id_drink_sales")
    private Long idDrinkSales;
    @JsonProperty ("user")
    private UsersResponseDTO usersResponseDTO;
    @JsonProperty ("drinks")
    private DrinksResponseDTO drinksResponseDTO;
    @JsonProperty ("total_drink_sales_price")
    private long totalDrinkSalesPrice;
    @JsonProperty ("date_drink_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateDrinkSales;
    @JsonProperty ("period")
    private String period;
}
