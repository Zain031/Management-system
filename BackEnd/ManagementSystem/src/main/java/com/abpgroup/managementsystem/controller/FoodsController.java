package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.FoodsRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.FoodsResponseDTO;
import com.abpgroup.managementsystem.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_FOOD)
@RequiredArgsConstructor
public class FoodsController {
    private final FoodService foodsService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createFood(@RequestBody FoodsRequestDTO foodRequestDTO) {
        try {
            FoodsResponseDTO foodResponseDTO = foodsService.createFoods(foodRequestDTO);
            CommonResponse<FoodsResponseDTO> commonResponse = CommonResponse.<FoodsResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new food")
                    .data(Optional.of(foodResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create food: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<CommonResponse<?>> getAllFoods() {
        try {
            List<FoodsResponseDTO> foodResponseDTOList = foodsService.getAllFoods();
            CommonResponse<List<FoodsResponseDTO>> commonResponse = CommonResponse.<List<FoodsResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all food")
                    .data(Optional.ofNullable(foodResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve food: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getFoodById(@PathVariable Long id) {
        try {
            FoodsResponseDTO foodResponseDTO = foodsService.getFoodsById(id);
            CommonResponse<FoodsResponseDTO> commonResponse = CommonResponse.<FoodsResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved food")
                    .data(Optional.of(foodResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve food: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateFood(@PathVariable Long id, @RequestBody FoodsRequestDTO foodRequestDTO) {
        try {
            FoodsResponseDTO foodResponseDTO = foodsService.updateFoods(id, foodRequestDTO);
            CommonResponse<FoodsResponseDTO> commonResponse = CommonResponse.<FoodsResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated food")
                    .data(Optional.of(foodResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update food: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteFood(@PathVariable Long id) {
        try {
            foodsService.deleteFoods(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted food")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete food: " + e.getMessage());
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
