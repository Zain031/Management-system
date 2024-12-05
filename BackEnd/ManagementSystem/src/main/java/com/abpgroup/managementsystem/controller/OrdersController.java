package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.OrdersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.OrdersResponseDTO;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.service.OrderService;
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
@RequestMapping(APIUrl.BASE_URL_ORDERS)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrdersController {
    private final OrderService orderService;
    private final OrdersRepository ordersRepository;
    @PostMapping("/create")
    public ResponseEntity<CommonResponse<?>> createOrder(@RequestBody OrdersRequestDTO ordersRequestDTO) {
        try {
            OrdersResponseDTO ordersResponseDTO = orderService.createOrder(ordersRequestDTO);
            CommonResponse<OrdersResponseDTO> commonResponse = CommonResponse.<OrdersResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created new order")
                    .data(Optional.of(ordersResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create order: " + e.getMessage());
        }
    }
    @GetMapping("/{id_order}")
    public ResponseEntity<CommonResponse<?>> getOrderById(@PathVariable (name = "id_order" ) Long id) {
        try {
            OrdersResponseDTO ordersResponseDTO = orderService.getOrderById(id);
            CommonResponse<OrdersResponseDTO> commonResponse = CommonResponse.<OrdersResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved order")
                    .data(Optional.of(ordersResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve order: " + e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllOrders(@RequestParam(name = "page",defaultValue = "0",required = true ) int page, @RequestParam(name = "size", defaultValue = "10", required = true ) int size) {
        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<OrdersResponseDTO> ordersResponseDTOList = orderService.getAllOrders(pageable);
            CommonResponse<Page<OrdersResponseDTO>> commonResponse = CommonResponse.<Page<OrdersResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all orders")
                    .data(Optional.of(ordersResponseDTOList))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve all orders: " + e.getMessage());
        }
    }
    @GetMapping("/month/{period}")
    public ResponseEntity<CommonResponse<?>> getOrdersByMonth(@PathVariable(name = "period") String period,@RequestParam(name = "year",required = true ) long year) {
        try {
            List<OrdersResponseDTO> ordersResponseDTO = orderService.getOrdersByPeriodAndYears(period.toUpperCase(),year);
            CommonResponse<List<OrdersResponseDTO>> commonResponse = CommonResponse.<List<OrdersResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved orders")
                    .data(Optional.of(ordersResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve orders: " + e.getMessage());
        }
    }
    @GetMapping("/export-pdf-date/{date_orders}")
    public ResponseEntity<byte[]> exportPdfByDate(@PathVariable(name = "date_orders", required = true) LocalDate dateOrders) {
        try {
            // Mengambil semua data pesanan berdasarkan tanggal pembelian
            List<OrdersResponseDTO> ordersList = orderService.getOrdersByDateOrders(dateOrders);

            // Menghasilkan PDF dalam bentuk byte array
            byte[] pdf = orderService.generatedPdfByDateOrders(ordersList, dateOrders);

            // Menambahkan header HTTP untuk file unduhan
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=orders-" + dateOrders + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF for the date " + dateOrders, e);
        }
    }

    @GetMapping("/export-pdf-month/{period}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable String period,@RequestParam(name = "year",defaultValue = "0",required = true ) long year) {
        try {
            List<OrdersResponseDTO> ordersList = orderService.getOrdersByPeriodAndYears(period.toUpperCase(),year);
            byte[] pdf = orderService.generatedPdfByPeriodAndYears(ordersList,period.toUpperCase(),year);

            // Set HTTP headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=orders-" + period.toLowerCase() + "-" + year + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF for the period " + period, e);
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
