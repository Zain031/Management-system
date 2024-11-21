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
public class LoginResponseDTO {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    private UsersResponseDTO user;

}
