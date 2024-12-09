package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.PaymentRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.PaymentResponseDTO;
import com.abpgroup.managementsystem.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_PAYMENT)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/process-payment")
    public ResponseEntity<CommonResponse<?>> processPayment(@RequestParam("id_order") Long orderId, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentResponseDTO paymentResponseDTO = paymentService.processPayment(orderId, paymentRequestDTO);
            CommonResponse<?> commonResponse = CommonResponse.<Object>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully processed payment")
                    .data(Optional.of(paymentResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponse.<Object>builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to process payment: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/generate-receipt/{id_order}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable(name = "id_order" ) Long orderId) {
        try {
            // Mendapatkan data PaymentResponseDTO berdasarkan orderId
            PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentById(orderId);

            // Menghasilkan struk kasir dalam bentuk PDF
            byte[] pdfData = paymentService.generatedReceipt(paymentResponseDTO);

            // Menyiapkan header untuk pengunduhan file PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=receipt_" + orderId + ".pdf");

            // Mengembalikan respons dengan file PDF
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfData);
        } catch (Exception e) {
            // Jika terjadi kesalahan, kembalikan status Internal Server Error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
