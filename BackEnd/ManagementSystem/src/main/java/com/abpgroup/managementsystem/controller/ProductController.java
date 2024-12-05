package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/{id_product}")
    public ResponseEntity<CommonResponse<?>> getProductById(@PathVariable ("id_product") Long id) {
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

    @PutMapping("/update/{id_product}")
    public ResponseEntity<CommonResponse<?>> updateProduct(@PathVariable (name = "id_product" ) Long id, @RequestBody ProductRequestDTO productRequestDTO) {
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

    @DeleteMapping("/delete/{id_product}")
    public ResponseEntity<CommonResponse<?>> deleteProduct(@PathVariable(name = "id_product" ) Long id) {
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
    public ResponseEntity<CommonResponse<?>> getAllProducts(@RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<ProductResponseDTO> productResponseDTOList = productService.getAllProducts(pageable);
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

    @GetMapping("/availability")
    public ResponseEntity<CommonResponse<?>> getProductByAvailability(@RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size, @RequestParam(name = "availability", defaultValue = "true", required = true ) boolean availability) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<ProductResponseDTO> productResponseDTOList = productService.getAllProductsByAvailableStock(pageable, availability);
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

    @GetMapping("/search/{product_name}")
    public ResponseEntity<CommonResponse<?>> getProductByProductName(@PathVariable(name = "product_name" ) String productName, @RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<ProductResponseDTO> productResponseDTOList = productService.getProductByProductName(productName, pageable);
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
