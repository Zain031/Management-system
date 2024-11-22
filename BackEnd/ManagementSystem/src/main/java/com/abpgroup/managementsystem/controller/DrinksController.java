package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.DrinksRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.DrinksResponseDTO;
import com.abpgroup.managementsystem.service.DrinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_DRINK)
@RequiredArgsConstructor
public class DrinksController {

    private final DrinksService drinksService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createDrinks(@RequestBody DrinksRequestDTO drinksRequestDTO) {
        try {
            DrinksResponseDTO drinksResponseDTO = drinksService.createDrinks(drinksRequestDTO);
            CommonResponse<DrinksResponseDTO> commonResponse = CommonResponse.<DrinksResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new drinks")
                    .data(Optional.of(drinksResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create drinks: " + e.getMessage());
        }
    }
    @GetMapping("/")
    public ResponseEntity<CommonResponse<?>> getAllDrinks() {
        try {
            List<DrinksResponseDTO> drinksResponseDTOList = drinksService.getAllDrinks();
            CommonResponse<List<DrinksResponseDTO>> commonResponse = CommonResponse.<List<DrinksResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all drinks")
                    .data(Optional.ofNullable(drinksResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve drinks: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getDrinksById(@PathVariable Long id) {
        try {
            DrinksResponseDTO drinksResponseDTO = drinksService.getDrinksById(id);
            CommonResponse<DrinksResponseDTO> commonResponse = CommonResponse.<DrinksResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved drinks")
                    .data(Optional.of(drinksResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve drinks: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateDrinks(@PathVariable Long id, @RequestBody DrinksRequestDTO drinksRequestDTO) {
        try {
            DrinksResponseDTO drinksResponseDTO = drinksService.updateDrinks(id, drinksRequestDTO);
            CommonResponse<DrinksResponseDTO> commonResponse = CommonResponse.<DrinksResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated drinks")
                    .data(Optional.of(drinksResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update drinks: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteDrinks(@PathVariable Long id) {
        try {
            drinksService.deleteDrinks(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted drinks")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete drinks: " + e.getMessage());
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
