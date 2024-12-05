package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @JsonProperty("payment_method")
    @Pattern(regexp = "^(QRIS|CASH)$", message = "Payment method must be QRIS or CASH")
    private String paymentMethod;

    @JsonProperty("amount")
    private Double amount;

}
