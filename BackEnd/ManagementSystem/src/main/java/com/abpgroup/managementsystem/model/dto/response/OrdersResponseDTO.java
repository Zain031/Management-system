package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponseDTO {

    @JsonProperty("id_order")
    private Long idOrder;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("order_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    @JsonProperty("period")
    private String period;

    @JsonProperty("years")
    private Long years;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_details")
    private List<OrderDetailResponseDTO> orderDetails;
}
