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
public class PurchaseRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Foodstuff ID is required")
    @JsonProperty("id_foodstuff")
    private Long idFoodstuff;

    @NotNull(message = "Tool ID is required")
    @JsonProperty("id_tool")
    private Long idTool;

    @NotNull(message = "Purchase total price is required")
    @Positive(message = "Purchase total price must be a positive number")
    @JsonProperty("purchase_total_price")
    private Long purchaseTotalPrice;

    @NotNull(message = "Purchase date is required")
    @JsonProperty("date_purchase")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePurchase;
}
