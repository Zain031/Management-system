package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsRequestDTO {

    @JsonProperty("id_product")
    private Long idProduct;

    @JsonProperty("quantity")
    private Long quantity;
}
