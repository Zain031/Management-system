package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {
    @JsonProperty("id_order_detail")
    private Long idOrderDetail;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("price_per_unit")
    private Double pricePerUnit;
    @JsonProperty("subtotal_price")
    private Double subtotalPrice;
}
