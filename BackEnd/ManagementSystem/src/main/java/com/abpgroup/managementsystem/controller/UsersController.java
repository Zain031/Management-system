package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.UsersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_USER)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UsersController {
    private final UserService userService;
    private final UsersRepository usersRepository;

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

    @PutMapping("/update/{id}")
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

    @GetMapping("/search/{name}")
    public ResponseEntity<CommonResponse<?>> getUserByName(@PathVariable String name, @RequestParam(name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<UsersResponseDTO> usersResponseDTOList = userService.getUserByName(name, PageRequest.of(page, size));
            CommonResponse<Page<UsersResponseDTO>> commonResponse = CommonResponse.<Page<UsersResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved users")
                    .data(Optional.ofNullable(usersResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve users: " + e.getMessage());
        }
    }
    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportUsersToPdf() {
        // Fetch users from database
        List<Users> users = usersRepository.findAll();

        // Generate PDF
        byte[] pdfContent = userService.generatedPdf(users);

        // Set HTTP headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    private ResponseEntity<CommonResponse<?>> createErrorResponse(HttpStatus status, String errorMessage) {
        CommonResponse<?> errorResponse = CommonResponse.builder()
                .statusCode(status.value())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
