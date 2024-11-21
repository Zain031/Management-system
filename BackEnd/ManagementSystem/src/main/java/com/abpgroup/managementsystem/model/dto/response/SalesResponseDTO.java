package com.abpgroup.managementsystem.model.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("id_food")
    private Long idFood;
    @JsonProperty("id_drink")
    private Long idDrink;
    @JsonProperty("total_sales_price")
    private Long totalSalesPrice;
    @JsonProperty("date_price")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePrice;

}
