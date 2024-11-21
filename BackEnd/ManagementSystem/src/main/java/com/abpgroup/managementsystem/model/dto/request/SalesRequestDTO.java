package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class SalesRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Food ID is required")
    @JsonProperty("id_food")
    private Long idFood;

    @NotNull(message = "Drink ID is required")
    @JsonProperty("id_drink")
    private Long idDrink;

    @NotNull(message = "Total sales price is required")
    @Positive(message = "Total sales price must be a positive number")
    @JsonProperty("total_sales_price")
    private Long totalSalesPrice;

    @NotNull(message = "Sales date is required")
    @JsonProperty("date_sales")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePrice;
}
