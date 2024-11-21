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
public class ProfitsResponseDTO {
    @JsonProperty("id_profit")
    private Long idProfit;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("id_sales")
    private Long idSales;
    @JsonProperty("id_purchase")
    private Long idPurchase;
    @JsonProperty("total_profit")
    private Long totalProfit;
    @JsonProperty("date_profit")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateProfit;
}
