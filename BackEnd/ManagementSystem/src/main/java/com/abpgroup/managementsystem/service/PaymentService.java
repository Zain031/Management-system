package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.PaymentRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.PaymentResponseDTO;
import com.abpgroup.managementsystem.model.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResponseDTO processPayment(String orderId, PaymentRequestDTO paymentRequestDTO);
    byte[] generatedReceipt(PaymentResponseDTO paymentResponseDTO);
    PaymentResponseDTO getPaymentById(String id, Orders orders);
    Page<PaymentResponseDTO> getAllPayments(Pageable pageable);
}
