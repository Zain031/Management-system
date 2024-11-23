package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.FoodSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.FoodSalesResponseDTO;
import com.abpgroup.managementsystem.service.FoodSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_SALES_FOODS)
@RequiredArgsConstructor
public class FoodSalesController {
    private final FoodSalesService foodSalesService;
    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createFoodSales(@RequestBody FoodSalesRequestDTO foodSalesRequestDTO) {
        try {
            FoodSalesResponseDTO foodSalesResponseDTO = foodSalesService.createFoodSales(foodSalesRequestDTO);
            CommonResponse<FoodSalesResponseDTO> commonResponse = CommonResponse.<FoodSalesResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new food sales")
                    .data(Optional.of(foodSalesResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create food sales: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllFoods() {
        try {
            List<FoodSalesResponseDTO> foodSalesResponseDTOList = foodSalesService.getAllFoodSales();
            CommonResponse<List<FoodSalesResponseDTO>> commonResponse = CommonResponse.<List<FoodSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all food sales")
                    .data(Optional.ofNullable(foodSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve food sales: " + e.getMessage());
        }
    }

    @GetMapping("/date/{date_food_sales}")
    public ResponseEntity<CommonResponse<?>> getFoodSalesById(@PathVariable LocalDate date_food_sales) {
        try {
            List<FoodSalesResponseDTO> foodSalesResponseDTOList = foodSalesService.getFoodSalesByDate(date_food_sales);
            CommonResponse<List<FoodSalesResponseDTO>> commonResponse = CommonResponse.<List<FoodSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved food sales")
                    .data(Optional.ofNullable(foodSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve food sales: " + e.getMessage());
        }
    }

    @GetMapping("/month/{period}")
    public ResponseEntity<CommonResponse<?>> getFoodSalesByMonth(@PathVariable String period) {
        try {
            List<FoodSalesResponseDTO> foodSalesResponseDTOList = foodSalesService.getFoodSalesByPeriod(period);
            CommonResponse<List<FoodSalesResponseDTO>> commonResponse = CommonResponse.<List<FoodSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved food sales")
                    .data(Optional.ofNullable(foodSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve food sales: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateFoodSales(@PathVariable Long id, @RequestBody FoodSalesRequestDTO foodSalesRequestDTO) {
        try {
            FoodSalesResponseDTO foodSalesResponseDTO = foodSalesService.updateFoodSales(id, foodSalesRequestDTO);
            CommonResponse<FoodSalesResponseDTO> commonResponse = CommonResponse.<FoodSalesResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated food sales")
                    .data(Optional.of(foodSalesResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update food sales: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteFoodSales(@PathVariable Long id) {
        try {
            foodSalesService.deleteFoodSales(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted food sales")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete food sales: " + e.getMessage());
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
