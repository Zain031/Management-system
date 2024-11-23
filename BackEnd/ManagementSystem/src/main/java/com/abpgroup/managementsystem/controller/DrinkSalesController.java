package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.DrinkSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.DrinkSalesResponseDTO;
import com.abpgroup.managementsystem.service.DrinkSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_SALES_DRINKS)
@RequiredArgsConstructor
public class DrinkSalesController {
    private final DrinkSalesService drinkSalesService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createDrinkSales(@RequestBody DrinkSalesRequestDTO drinkSalesRequestDTO) {
        try {
            DrinkSalesResponseDTO drinkSalesResponseDTO = drinkSalesService.createDrinkSales(drinkSalesRequestDTO);
            CommonResponse<DrinkSalesResponseDTO> commonResponse = CommonResponse.<DrinkSalesResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new drink sales")
                    .data(Optional.of(drinkSalesResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create drink sales: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllDrinks() {
        try {
            List<DrinkSalesResponseDTO> drinkSalesResponseDTOList = drinkSalesService.getAllDrinkSales();
            CommonResponse<List<DrinkSalesResponseDTO>> commonResponse = CommonResponse.<List<DrinkSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all drink sales")
                    .data(Optional.ofNullable(drinkSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve drink sales: " + e.getMessage());
        }
    }

    @GetMapping("/date/{date_drink_sales}")
    public ResponseEntity<CommonResponse<?>> getDrinkSalesById(@PathVariable LocalDate date_drink_sales) {
        try {
            List<DrinkSalesResponseDTO> drinkSalesResponseDTOList = drinkSalesService.getDrinkSalesByDate(date_drink_sales);
            CommonResponse<List<DrinkSalesResponseDTO>> commonResponse = CommonResponse.<List<DrinkSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved drink sales")
                    .data(Optional.ofNullable(drinkSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve drink sales: " + e.getMessage());
        }
    }

    @GetMapping("/month/{period}")
    public ResponseEntity<CommonResponse<?>> getDrinkSalesByMonth(@PathVariable String period) {
        try {
            List<DrinkSalesResponseDTO> drinkSalesResponseDTOList = drinkSalesService.getDrinkSalesByPeriod(period);
            CommonResponse<List<DrinkSalesResponseDTO>> commonResponse = CommonResponse.<List<DrinkSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved drink sales")
                    .data(Optional.ofNullable(drinkSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve drink sales: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateDrinkSales(@PathVariable Long id, @RequestBody DrinkSalesRequestDTO drinkSalesRequestDTO) {
        try {
            DrinkSalesResponseDTO drinkSalesResponseDTO = drinkSalesService.updateDrinkSales(id, drinkSalesRequestDTO);
            CommonResponse<DrinkSalesResponseDTO> commonResponse = CommonResponse.<DrinkSalesResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated drink sales")
                    .data(Optional.of(drinkSalesResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update drink sales: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteDrinkSales(@PathVariable Long id) {
        try {
            drinkSalesService.deleteDrinkSales(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted drink sales")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete drink sales: " + e.getMessage());
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
