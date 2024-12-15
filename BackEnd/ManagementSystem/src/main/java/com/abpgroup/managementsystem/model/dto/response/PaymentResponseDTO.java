package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    @JsonProperty("id_payment")
    private Long idPayment;

    @JsonProperty("method")
    private String method;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("change")
    private Double change;

    @JsonProperty("link_qris")
    private String linkQris;

    @JsonProperty("status_midtrans")
    private String statusMidtrans;

    @JsonProperty("order")
    private OrdersResponseDTO order;

    @JsonProperty("payment_date")
    private LocalDateTime paymentDate;
}
