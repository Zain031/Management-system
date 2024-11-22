package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.LoginRequestDTO;
import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.LoginResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.service.AuthService;
import com.abpgroup.managementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_USER)
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody UsersRequestDTO usersRequestDTO) {
        try {
            UsersResponseDTO usersResponseDTO = userService.register(usersRequestDTO);
            CommonResponse<UsersResponseDTO> commonResponse = CommonResponse.<UsersResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully registered new user")
                    .data(Optional.of(usersResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to register user: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResponse<?>> getAllUsers() {
        try {
            List<UsersResponseDTO> usersResponseDTOList = userService.getAllUsers();
            CommonResponse<List<UsersResponseDTO>> commonResponse = CommonResponse.<List<UsersResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all users")
                    .data(Optional.ofNullable(usersResponseDTOList))
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve users: " + e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CommonResponse<?>> getUserById(@PathVariable Long id) {
        try {
            UsersResponseDTO usersResponseDTO = userService.findById(id);
            CommonResponse<UsersResponseDTO> commonResponse = CommonResponse.<UsersResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved user")
                    .data(Optional.of(usersResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "User not found: " + e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<CommonResponse<?>> updateUser(@PathVariable Long id, @RequestBody UsersRequestDTO usersRequestDTO) {
        try {
            UsersResponseDTO usersResponseDTO = userService.update(id, usersRequestDTO);
            CommonResponse<UsersResponseDTO> commonResponse = CommonResponse.<UsersResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated user")
                    .data(Optional.of(usersResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<CommonResponse<?>> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            CommonResponse<UsersResponseDTO> commonResponse = CommonResponse.<UsersResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted user")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "Failed to delete user: " + e.getMessage());
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
