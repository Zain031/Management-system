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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitsRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Sales ID is required")
    @JsonProperty("id_sales")
    private Long idSales;

    @NotNull(message = "Purchase ID is required")
    @JsonProperty("id_purchase")
    private Long idPurchase;

    @NotNull(message = "Total profit is required")
    @Positive(message = "Total profit must be a positive number")
    @JsonProperty("total_profit")
    private Long totalProfit;

    @NotNull(message = "Profit date is required")
    @JsonProperty("date_profit")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateProfit;
}
