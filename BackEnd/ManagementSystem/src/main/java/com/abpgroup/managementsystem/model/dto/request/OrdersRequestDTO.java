package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersRequestDTO {

    @JsonProperty("customer_name")
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @JsonProperty("order_details")
    private List<OrderDetailsRequestDTO> orderDetailsRequestDTOList;
}
