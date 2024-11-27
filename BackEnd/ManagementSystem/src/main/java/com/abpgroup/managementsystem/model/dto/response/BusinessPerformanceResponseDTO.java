package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
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
public class BusinessPerformanceResponseDTO {
    @JsonProperty("total_profit")
    private Long totalProfit;
    @JsonProperty("total_lost_profit")
    private Long totalLostProfit;
    @JsonProperty("period")
    private String period;
    @JsonProperty("years")
    private Long years;
}
