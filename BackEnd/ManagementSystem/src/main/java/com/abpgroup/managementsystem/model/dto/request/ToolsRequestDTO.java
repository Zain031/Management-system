package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class ToolsRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Tool name is required")
    @Size(min = 1, max = 100, message = "Tool name must be between 1 and 100 characters")
    @JsonProperty("tool_name")
    private String toolName;

    @NotNull(message = "Tool price unit is required")
    @Positive(message = "Tool price unit must be a positive number")
    @JsonProperty("tool_price_unit")
    private Integer toolPriceUnit;

    @NotNull(message = "Tool quantity is required")
    @Positive(message = "Tool quantity must be a positive number")
    @JsonProperty("tool_quantity")
    private Integer toolQuantity;

    @NotNull(message = "Tool discount is required")
    @Positive(message = "Tool discount must be a positive number")
    @JsonProperty("tool_discount")
    private Integer toolDiscount;

    @NotNull(message = "Date of tool purchase is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_tool_buy")
    private LocalDate dateToolBuy;
}
