package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPerformanceResponseDTO {

    @JsonProperty("total_profit")
    private long totalProfit;
    @JsonProperty("grand_total_order")
    private long grandTotalOrder;

    @JsonProperty("grand_total_price_order")
    private long grandTotalPriceOrder;

    @JsonProperty("grand_total_price_inventory")
    private long grandTotalPriceInventory;

    @JsonProperty("years")
    private Long years;

    @JsonProperty("monthly_performance")
    private List<MonthlyPerformance> monthlyPerformances;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyPerformance {
        @JsonProperty("month")
        private String month;

        @JsonProperty("profit")
        private long profit;

        @JsonProperty("total_order")
        private long totalOrder;

        @JsonProperty("total_price_order")
        private long totalPriceOrder;

        @JsonProperty("total_price_inventory")
        private long totalPriceInventory;
    }
}
