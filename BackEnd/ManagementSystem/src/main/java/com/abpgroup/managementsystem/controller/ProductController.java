package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);
            CommonResponse<ProductResponseDTO> commonResponse = CommonResponse.<ProductResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new product")
                    .data(Optional.of(productResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create product: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getProductById(@PathVariable Long id) {
        try {
            ProductResponseDTO productResponseDTO = productService.getProductById(id);
            CommonResponse<ProductResponseDTO> commonResponse = CommonResponse.<ProductResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved product")
                    .data(Optional.of(productResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve product: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<CommonResponse<?>> getProductByCategory(@PathVariable String category,@RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<ProductResponseDTO> productResponseDTOList = productService.getProductByCategory(category, pageable);
            CommonResponse<Page<ProductResponseDTO>> commonResponse = CommonResponse.<Page<ProductResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved products")
                    .data(Optional.of(productResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve products: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponseDTO = productService.updateProduct(id, productRequestDTO);
            CommonResponse<ProductResponseDTO> commonResponse = CommonResponse.<ProductResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated product")
                    .data(Optional.of(productResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update product: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted product")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete product: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllProductsByPage(@RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<ProductResponseDTO> productResponseDTOList = productService.getAllProductsByPage(pageable);
            if (productResponseDTOList.isEmpty()) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "No products found");
            } else {
                CommonResponse<Page<ProductResponseDTO>> commonResponse = CommonResponse.<Page<ProductResponseDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully retrieved products")
                        .data(Optional.of(productResponseDTOList))
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
            }
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve products: " + e.getMessage());
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
