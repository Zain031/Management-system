package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolsResponseDTO {
    @JsonProperty("id_tool")
    private Long idTool;
    @JsonProperty("id_user")
    private Long idUser;
    @JsonProperty("tool_name")
    private String toolName;
    @JsonProperty("tool_price_unit")
    private Integer toolPriceUnit;
    @JsonProperty("tool_quantity")
    private Integer toolQuantity;
    @JsonProperty("tool_discount")
    private Integer toolDiscount;
    @JsonProperty("tool_price_discount")
    private Integer toolPriceDiscount;
    @JsonProperty("tool_total_price")
    private Integer toolTotalPrice;
    @JsonProperty("date_tool_buy")
    private LocalDate dateToolBuy;
}
