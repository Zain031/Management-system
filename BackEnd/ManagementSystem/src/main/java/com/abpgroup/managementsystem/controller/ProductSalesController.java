package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.service.ProductSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_SALES_PRODUCT)
@RequiredArgsConstructor
public class ProductSalesController {
    private final ProductSalesService productSalesService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createProductSales(@RequestBody ProductSalesRequestDTO productSalesRequestDTO) {
        try {
            ProductSalesResponseDTO productSalesResponseDTO = productSalesService.createProductSales(productSalesRequestDTO);
            CommonResponse<ProductSalesResponseDTO> commonResponse = CommonResponse.<ProductSalesResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new product sales")
                    .data(Optional.of(productSalesResponseDTO))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create product sales: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllProducts() {
        try {
            List<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getAllProductSales();
            CommonResponse<List<ProductSalesResponseDTO>> commonResponse = CommonResponse.<List<ProductSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all product sales")
                    .data(Optional.ofNullable(productSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product sales: " + e.getMessage());
        }
    }

    @GetMapping("/date/{date_product_sales}")
    public ResponseEntity<CommonResponse<?>> getProductSalesById(@PathVariable LocalDate date_product_sales) {
        try {
            List<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByDate(date_product_sales);
            CommonResponse<List<ProductSalesResponseDTO>> commonResponse = CommonResponse.<List<ProductSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved product sales")
                    .data(Optional.ofNullable(productSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product sales: " + e.getMessage());
        }
    }

    @GetMapping("/month/{period}")
    public ResponseEntity<CommonResponse<?>> getProductSalesByMonth(@PathVariable String period) {
        try {
            List<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByPeriod(period);
            CommonResponse<List<ProductSalesResponseDTO>> commonResponse = CommonResponse.<List<ProductSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved product sales")
                    .data(Optional.ofNullable(productSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product sales: " + e.getMessage());
        }
    }

    @GetMapping("/category/{product_categories}")
    public ResponseEntity<CommonResponse<?>> getProductSalesByProductCategories(@PathVariable String product_categories) {
        try {
            List<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByProductCategories(product_categories);
            CommonResponse<List<ProductSalesResponseDTO>> commonResponse = CommonResponse.<List<ProductSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved product sales")
                    .data(Optional.ofNullable(productSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product sales: " + e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateProductSales(@PathVariable Long id, @RequestBody ProductSalesRequestDTO productSalesRequestDTO) {
        try {
            ProductSalesResponseDTO productSalesResponseDTO = productSalesService.updateProductSales(id, productSalesRequestDTO);
            CommonResponse<ProductSalesResponseDTO> commonResponse = CommonResponse.<ProductSalesResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated product sales")
                    .data(Optional.of(productSalesResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update product sales: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteProductSales(@PathVariable Long id) {
        try {
            productSalesService.deleteProductSales(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted product sales")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete product sales: " + e.getMessage());
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
