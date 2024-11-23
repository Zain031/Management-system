package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodsRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("id_user")
    private Long idUser;

    @NotNull(message = "Food name is required")
    @Size(min = 1, max = 100, message = "Food name must be between 1 and 100 characters")
    @JsonProperty("food_name")
    private String foodName;

    @NotNull(message = "Food price is required")
    @Positive(message = "Food price must be a positive number")
    @JsonProperty("food_price")
    private Long foodPrice;

}
