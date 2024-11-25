package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesResponseDTO {

    @JsonProperty("id_sales")
    private Long idSales;

    @JsonProperty("total_leftover_sales")
    private Long totalLeftoverSales;

    @JsonProperty("total_sales")
    private Long totalSales;

    @JsonProperty("total_sales_price")
    private Long totalSalesPrice;

    @JsonProperty("total_leftover_sales_price")
    private Long totalLeftoverSalesPrice;

    @JsonProperty("date_sales")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateSales;

    @JsonProperty("period")
    private String period;

}
