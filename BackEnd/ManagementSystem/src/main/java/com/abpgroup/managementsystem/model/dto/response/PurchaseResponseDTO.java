package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PurchaseResponseDTO {
    @JsonProperty("purchase_total_quantity")
    private Double purchaseTotalQuantity;
    @JsonProperty("purchase_total_price")
    private Double purchaseTotalPrice;
    @JsonProperty("date_purchase")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePurchase;
    @JsonProperty("period")
    private String period;
    @JsonProperty("years")
    private Long years;
}
