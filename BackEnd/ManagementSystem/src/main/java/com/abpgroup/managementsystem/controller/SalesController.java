package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;
import com.abpgroup.managementsystem.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_SALES)
@RequiredArgsConstructor
public class SalesController {
    private final SalesService salesService;

    @GetMapping("/date/{date_sales}")
    public ResponseEntity<CommonResponse<?>> getSalesByDate(@PathVariable(value = "date_sales",required = true) LocalDate date_sales) {
        try {
            SalesResponseDTO salesResponseDTOList = salesService.getSalesByDate(date_sales);
            CommonResponse<SalesResponseDTO> commonResponse = CommonResponse.<SalesResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved sales")
                    .data(Optional.ofNullable(salesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve sales: " + e.getMessage());
        }
    }

    @GetMapping("/period/{period}")
    public ResponseEntity<CommonResponse<?>> getSalesByPeriod(@PathVariable String period) {
        try {
            SalesResponseDTO salesResponseDTOList = salesService.getSalesByPeriod(period);
            CommonResponse<SalesResponseDTO> commonResponse = CommonResponse.<SalesResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved sales")
                    .data(Optional.ofNullable(salesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve sales: " + e.getMessage());
        }
    }

    private ResponseEntity<CommonResponse<?>> createErrorResponse(HttpStatus status, String message) {
        CommonResponse<Object> commonResponse = CommonResponse.<Object>builder()
                .statusCode(status.value())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(commonResponse);
    }

}
