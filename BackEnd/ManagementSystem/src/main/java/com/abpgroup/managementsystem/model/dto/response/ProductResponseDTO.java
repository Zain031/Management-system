package com.abpgroup.managementsystem.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    @JsonProperty("id_product")
    private Long idProduct;

    @JsonProperty("user")
    private UsersResponseDTO usersResponseDTO;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_price")
    private Long productPrice;

    @JsonProperty("categories")
    private String categories;
}
