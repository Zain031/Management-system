package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.service.BusinessPerformanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_BUSINESS_PERFORMANCE)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BusinessPerformanceController {
    private final BusinessPerformanceService businessPerformanceService;

    @GetMapping("/period/{period}")
    public ResponseEntity<CommonResponse<?>> getBusinessPerformanceByPeriod(@PathVariable String period, @RequestParam(name = "years", required = false) Long years) {
        try {
            BusinessPerformanceResponseDTO businessPerformanceResponseDTO = businessPerformanceService.getBusinessPerformanceByPeriodAndYears(period.toUpperCase(), years);
            CommonResponse<BusinessPerformanceResponseDTO> commonResponse = CommonResponse.<BusinessPerformanceResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved business performance")
                    .data(Optional.of(businessPerformanceResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve business performance: " + e.getMessage());
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