package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.ProductSalesRepository;
import com.abpgroup.managementsystem.service.ProductSalesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_SALES_PRODUCT)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductSalesController {
    private final ProductSalesService productSalesService;
    private final ProductSalesRepository productSalesRepository;

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
    public ResponseEntity<CommonResponse<?>> getAllProductSales(@RequestParam(name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getAllProductSales(PageRequest.of(page, size));
            CommonResponse<Page<ProductSalesResponseDTO>> commonResponse = CommonResponse.<Page<ProductSalesResponseDTO>>builder()
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
    public ResponseEntity<CommonResponse<?>> getProductSalesById(@PathVariable LocalDate date_product_sales, @RequestParam(name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByDate(date_product_sales, PageRequest.of(page, size));
            CommonResponse<Page<ProductSalesResponseDTO>> commonResponse = CommonResponse.<Page<ProductSalesResponseDTO>>builder()
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
    public ResponseEntity<CommonResponse<?>> getProductSalesByMonth(@PathVariable String period,@RequestParam(name = "years", required = true) long years, @RequestParam( name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByPeriodAndYears(period,years, PageRequest.of(page, size));
            CommonResponse<Page<ProductSalesResponseDTO>> commonResponse = CommonResponse.<Page<ProductSalesResponseDTO>>builder()
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
    public ResponseEntity<CommonResponse<?>> getProductSalesByProductCategories(@PathVariable String product_categories, @RequestParam(name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByProductCategories(product_categories, PageRequest.of(page, size));
            CommonResponse<Page<ProductSalesResponseDTO>> commonResponse = CommonResponse.<Page<ProductSalesResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved product sales")
                    .data(Optional.ofNullable(productSalesResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product sales: " + e.getMessage());
        }
    }

    @GetMapping("/search/{productName}")
    public ResponseEntity<CommonResponse<?>> getProductByProductName(@PathVariable String productName, @RequestParam(name = "page", defaultValue = "0" ) int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<ProductSalesResponseDTO> productSalesResponseDTOList = productSalesService.getProductSalesByProductName(productName, PageRequest.of(page, size));
            CommonResponse<Page<ProductSalesResponseDTO>> commonResponse = CommonResponse.<Page<ProductSalesResponseDTO>>builder()
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
    @GetMapping("/export-pdf-month/{period}")
    public ResponseEntity<byte[]> exportProductSalesToPdf(@PathVariable String period, @RequestParam(name = "years", required = true) long years) {
        // Fetch product sales from database
        List< ProductSales> productSales = productSalesRepository.getProductSalesByPeriodAndYears(period.toUpperCase(), years);

        // Generate PDF
        byte[] pdfContent = productSalesService.generatedPdf(productSales, period, years);

        // Set HTTP headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=product-sales-" + period + "-" + years + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/export-pdf-date/{date}")
    public ResponseEntity<?> exportProductSalesToPdf(@PathVariable LocalDate date) {
        try {
            // Validasi input date
            if (date == null) {
                return ResponseEntity.badRequest().body("Date parameter cannot be null.");
            }

            // Fetch product sales data from the database
            List<ProductSales> productSales = productSalesRepository.getProductSalesByDateProductSales(date);

            // Jika data tidak ditemukan
            if (productSales == null || productSales.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No product sales data found for the given date: " + date);
            }

            // Generate PDF
            byte[] pdfContent = productSalesService.generatedPdfByDate(productSales, date);

            // Set HTTP headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=product-sales-" + date + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfContent);

        } catch (Exception e) {
            // Tangkap error dan berikan respons
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating the PDF: " + e.getMessage());
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
