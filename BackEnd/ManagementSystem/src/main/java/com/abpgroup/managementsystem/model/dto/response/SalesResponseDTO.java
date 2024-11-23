package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesResponseDTO {
    @JsonProperty("id_sales")
    private Long idSales;

    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;

    @JsonProperty("food_sales")
    private FoodSalesResponseDTO foodSalesResponseDTO;

    @JsonProperty("drink_sales")
    private DrinkSalesResponseDTO drinksResponseDTO;

    @JsonProperty("total_sales_price")
    private Long totalSalesPrice;

    @JsonProperty("date_sales_price")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateSalesPrice;

    @JsonProperty("period")
    private String period;

}
