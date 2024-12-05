package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.MidtransWebhookRequestDTO;
import com.abpgroup.managementsystem.model.entity.Orders;
import com.abpgroup.managementsystem.model.entity.Payments;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.repository.PaymentsRepository;
import com.abpgroup.managementsystem.service.WebhookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final PaymentsRepository paymentsRepository;
    private final OrdersRepository ordersRepository;

    @Transactional
    public void processWebhook(MidtransWebhookRequestDTO webhookRequest) {
        Payments payment = paymentsRepository.findByOrderId(webhookRequest.getOrderId());
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found with order ID: " + webhookRequest.getOrderId());
        }
        Orders order = payment.getOrder();

        // Update status berdasarkan webhook
        payment.setStatusMidtrans(webhookRequest.getTransactionStatus());
        updateOrderStatusBasedOnMidtrans(order, webhookRequest.getTransactionStatus());

        paymentsRepository.save(payment);
        ordersRepository.save(order);
    }

    private void updateOrderStatusBasedOnMidtrans(Orders order, String midtransTransactionResponse) {
        switch (midtransTransactionResponse) {
            case "settlement":
                order.setStatus(Orders.OrderStatus.COMPLETED);
                break;
            case "deny":
            case "cancel":
            case "expire":
                order.setStatus(Orders.OrderStatus.CANCELED);
                break;
            case "pending":
                order.setStatus(Orders.OrderStatus.PENDING);
                break;
            default:
                throw new IllegalArgumentException("Unknown transaction status: " + midtransTransactionResponse);
        }
    }
}
