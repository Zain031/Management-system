package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.PurchaseResponseDTO;
import com.abpgroup.managementsystem.service.PurchaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_PURCHASE)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PurchaseController {
    private final PurchaseService purchaseService;
    @GetMapping("/date/{date_purchase}")
    public ResponseEntity<CommonResponse<?>> getPurchaseByDate(@PathVariable("date_purchase") LocalDate datePurchase) {
        try {
            PurchaseResponseDTO purchaseResponseDTO = purchaseService.getPurchasesByDate(datePurchase);
            CommonResponse<PurchaseResponseDTO> commonResponse = CommonResponse.<PurchaseResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved purchase")
                    .data(Optional.of(purchaseResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve purchase: " + e.getMessage());
        }

    }

    @GetMapping("/month/{period}")
    public ResponseEntity<CommonResponse<?>> getPurchaseByPeriod(@PathVariable String period, @RequestParam(name = "years", required = false) Long years) {
        try {
            PurchaseResponseDTO purchaseResponseDTO = purchaseService.getPurchasesByPeriodAndYears(period, years);
            CommonResponse<PurchaseResponseDTO> commonResponse = CommonResponse.<PurchaseResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved purchase")
                    .data(Optional.of(purchaseResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve purchase: " + e.getMessage());
        }

    }

    private ResponseEntity<CommonResponse<?>> createErrorResponse(HttpStatus httpStatus, String message) {
        CommonResponse<Object> commonResponse = CommonResponse.<Object>builder()
                .statusCode(httpStatus.value())
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(commonResponse);
    }

}
