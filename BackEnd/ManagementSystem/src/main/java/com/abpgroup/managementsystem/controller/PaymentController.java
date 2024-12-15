package com.abpgroup.managementsystem.controller;

import com.abpgroup.managementsystem.constant.APIUrl;
import com.abpgroup.managementsystem.model.dto.request.PaymentRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.CommonResponse;
import com.abpgroup.managementsystem.model.dto.response.PaymentResponseDTO;
import com.abpgroup.managementsystem.model.entity.Orders;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.BASE_URL_PAYMENT)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrdersRepository ordersRepository;

    @PostMapping("/process-payment")
    public ResponseEntity<CommonResponse<?>> processPayment(@RequestParam("id_order") String orderId, @RequestBody PaymentRequestDTO paymentRequestDTO) {
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

    @GetMapping("/status/{id_order}")
    public ResponseEntity<CommonResponse<?>> getStatus(@PathVariable("id_order") String id) {
        try {
            Orders orders = ordersRepository.findById(id).orElse(null);
            PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentById(id, orders);
            CommonResponse<PaymentResponseDTO> commonResponse = CommonResponse.<PaymentResponseDTO>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved payment status")
                    .data(Optional.of(paymentResponseDTO))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponse.<Object>builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve payment status: " + e.getMessage())
                    .build());
        }

    }

    @GetMapping("/generate-receipt/{id_order}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable(name = "id_order") String orderId) {
        // Mendapatkan data PaymentResponseDTO berdasarkan orderId
        Orders orders = ordersRepository.findById(orderId).orElse(null);
        PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentById(orderId, orders);

        // Menghasilkan struk kasir dalam bentuk PDF
        byte[] pdfData = paymentService.generatedReceipt(paymentResponseDTO);

        // Tentukan folder tujuan untuk menyimpan file PDF
        String folderPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Project-Fanny" + File.separator + "Data-Receipt";
        Path path = Paths.get(folderPath);

        // Periksa apakah folder sudah ada, jika belum, buat folder
        File dir = path.toFile();
        if (!dir.exists()) {
            boolean created = dir.mkdirs();  // Membuat folder jika belum ada
            if (created) {
                System.out.println("Folder berhasil dibuat di: " + path.toString());
            } else {
                System.out.println("Gagal membuat folder di: " + path.toString());
            }
        } else {
            System.out.println("Folder sudah ada di: " + path.toString());
        }

        // Tentukan nama file PDF
        String fileName = "receipt_" + orderId + ".pdf";
        File file = new File(path.toFile(), fileName);

        // Menyimpan file PDF ke dalam folder
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdfData);
            System.out.println("File berhasil disimpan di: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan file: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Menyiapkan header untuk pengunduhan file PDF
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        // Mengembalikan respons dengan file PDF
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfData);

    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<?>> getAllPayments(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PaymentResponseDTO> paymentResponseDTOS = paymentService.getAllPayments(pageable);
            CommonResponse<Page<PaymentResponseDTO>> commonResponse = CommonResponse.<Page<PaymentResponseDTO>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved all payments")
                    .data(Optional.of(paymentResponseDTOS))
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponse.<Object>builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve all payments: " + e.getMessage())
                    .build());
        }
    }


}

