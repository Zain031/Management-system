package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import com.abpgroup.managementsystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_INVENTORY)
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createInventory(@RequestBody InventoryRequestDTO requestDTO) {
        try{
            InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(requestDTO);
            CommonResponse<InventoryResponseDTO> commonResponse = CommonResponse.<InventoryResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new material")
                    .data(Optional.of(inventoryResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        }catch (Exception e){
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create inventory: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<CommonResponse<?>> getAllInventory(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<InventoryResponseDTO> inventoryResponseDTOList = inventoryService.getAllInventory(pageable);
            CommonResponse<Page<InventoryResponseDTO>> commonResponse = CommonResponse.<Page<InventoryResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all inventories")
                    .data(Optional.of(inventoryResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve inventories: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getInventoryById(@PathVariable Long id) {
        try {
            InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventoryById(id);
            CommonResponse<InventoryResponseDTO> commonResponse = CommonResponse.<InventoryResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved inventory")
                    .data(Optional.of(inventoryResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve inventory: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<CommonResponse<?>> getInventoryByCategory(
            @PathVariable String category,
            @RequestParam(name = "page",defaultValue = "0",required = true ) int page,
            @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<InventoryResponseDTO> inventoryResponseDTOList = inventoryService.getAllInventoryByCategory(pageable, category);
            CommonResponse<Page<InventoryResponseDTO>> commonResponse = CommonResponse.<Page<InventoryResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved inventorys")
                    .data(Optional.of(inventoryResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve inventorys: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<?>> updateInventory(@PathVariable Long id, @RequestBody InventoryRequestDTO inventoryRequestDTO) {
        try {
            InventoryResponseDTO inventoryResponseDTO = inventoryService.updateInventory(id, inventoryRequestDTO);
            CommonResponse<InventoryResponseDTO> commonResponse = CommonResponse.<InventoryResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully updated inventory")
                    .data(Optional.of(inventoryResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update inventory: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<?>> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventoryById(id);
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted inventory")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete inventory: " + e.getMessage());
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
