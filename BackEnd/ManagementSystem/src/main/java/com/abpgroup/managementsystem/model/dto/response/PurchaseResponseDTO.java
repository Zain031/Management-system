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
    @JsonProperty("id_purchase")
    private Long idPurchase;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("id_foodstuff")
    private Long idFoodstuff;
    @JsonProperty("id_tool")
    private Long idTool;
    @JsonProperty("purchase_total_price")
    private Long purchaseTotalPrice;
    @JsonProperty("date_purchase")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePurchase;
}
