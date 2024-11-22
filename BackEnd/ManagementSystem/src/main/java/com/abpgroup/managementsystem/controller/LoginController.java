package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;
import com.abpgroup.managementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_USER)
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
            CommonResponse<LoginResponseDTO> commonResponse = CommonResponse.<LoginResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully logged in")
                    .data(Optional.of(loginResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to login: " + e.getMessage());
        }
    }

    private ResponseEntity<CommonResponse<?>> createErrorResponse(HttpStatus status, String errorMessage) {
        CommonResponse<?> errorResponse = CommonResponse.builder()
                .statusCode(status.value())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
