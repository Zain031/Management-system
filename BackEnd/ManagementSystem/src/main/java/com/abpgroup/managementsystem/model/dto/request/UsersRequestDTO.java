package com.abpgroup.managementsystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersRequestDTO {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @JsonProperty("email")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @JsonProperty("password")
    private String password;
    @NotBlank(message = "Name cannot be blank")
    @JsonProperty("name")
    private String name;
    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "SUPER_ADMIN|ADMIN", message = "Role must be SUPER_ADMIN or ADMIN")
    @JsonProperty("role")
    private String role;
}
