package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPerformanceResponseDTO {
    @JsonProperty("total_profit")
    private Long totalProfit;
    @JsonProperty("period")
    private String period;
    @JsonProperty("years")
    private Long years;
}
