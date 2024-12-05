package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.PaymentRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(Long orderId, PaymentRequestDTO paymentRequestDTO);
    byte[] generatedReceipt(PaymentResponseDTO paymentResponseDTO);
    PaymentResponseDTO getPaymentById(Long id);
}
