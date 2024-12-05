package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MidtransWebhookRequestDTO {
    @JsonProperty ("id_order")
    private Long orderId;

    @JsonProperty ("transaction_status")
    @Pattern(regexp = "settlement|pending|deny|cancel|expire")
    private String transactionStatus;    // settlement, pending, deny, cancel, expire

    @JsonProperty ("payment_type")
    @Pattern(regexp = "QRIS")
    private String paymentType; //QRIS
}
