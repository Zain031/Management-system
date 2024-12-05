package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.PaymentRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.OrderDetailResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.OrdersResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.PaymentResponseDTO;
import com.abpgroup.managementsystem.model.entity.OrderDetails;
import com.abpgroup.managementsystem.model.entity.Orders;
import com.abpgroup.managementsystem.model.entity.Payments;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.repository.PaymentsRepository;
import com.abpgroup.managementsystem.service.MidtransService;
import com.abpgroup.managementsystem.service.PaymentService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrdersRepository ordersRepository;
    private final PaymentsRepository paymentsRepository;
    private final MidtransService midtransService;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Override
    @Transactional
    public PaymentResponseDTO processPayment(Long orderId, PaymentRequestDTO paymentRequestDTO) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Validasi status order
        if (order.getStatus().equals(Orders.OrderStatus.COMPLETED)) {
            throw new IllegalArgumentException("Order already completed with ID: " + orderId);
        }

        try {
            Payments.PaymentMethod paymentMethod = Payments.PaymentMethod.valueOf(paymentRequestDTO.getPaymentMethod());

            // Validasi jumlah pembayaran berdasarkan metode pembayaran
            validatePaymentAmount(paymentRequestDTO, order, paymentMethod);

            String qrisTransactionResponse = null; // QRIS response (if applicable)
            String midtransTransactionResponse = "pending"; // Default to "pending"

            if (paymentMethod == Payments.PaymentMethod.QRIS) {
                double amount = paymentRequestDTO.getAmount()*0.007;
                long amountLong = Math.round(paymentRequestDTO.getAmount())+Math.round(amount);
                qrisTransactionResponse = midtransService.createQrisTransaction(String.valueOf(orderId), amountLong);
                LocalDate now = LocalDate.now();
                String idOrder = now + "_UjiCoba_" + orderId;
                midtransTransactionResponse = midtransService.getTransactionStatus(idOrder);

                // Log the transaction response for debugging
                logger.info("QRIS Transaction Response: {}", midtransTransactionResponse);

                // Update status order based on midtransTransactionResponse
                updateOrderStatusBasedOnMidtrans(order, midtransTransactionResponse);
            }

            if (paymentMethod == Payments.PaymentMethod.CASH) {
                // For cash payments, settle immediately
                midtransTransactionResponse = "settlement";
                updateOrderStatusBasedOnMidtrans(order, midtransTransactionResponse);
            }

            // Save payment details
            Payments payment = Payments.builder()
                    .order(order)
                    .amount(paymentRequestDTO.getAmount())
                    .method(paymentMethod)
                    .paymentDate(LocalDateTime.now()) // Adjust if needed
                    .qrisResponse(qrisTransactionResponse)
                    .statusMidtrans(midtransTransactionResponse) // Default is "pending"
                    .build();

            paymentsRepository.save(payment);

            return convertToResponse(payment);

        } catch (IllegalArgumentException e) {
            // Validation error (e.g., invalid payment method)
            throw e;

        } catch (Exception e) {
            // Log the error before throwing the exception
            logger.error("Failed to process payment for orderId: {}. Error: {}", orderId, e.getMessage(), e);

            // If an error occurs, set order status to CANCELED
            order.setStatus(Orders.OrderStatus.CANCELED);
            ordersRepository.save(order);

            // Throw a more specific exception
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    private void updateOrderStatusBasedOnMidtrans(Orders order, String midtransTransactionResponse) {
        // Log the transaction status for debugging
        logger.info("Updating order status based on midtrans transaction response: {}", midtransTransactionResponse);

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
                // Log the unknown transaction status
                logger.error("Unknown transaction status received: {}", midtransTransactionResponse);
//                order.setStatus(Orders.OrderStatus.CANCELED); // Optionally set it to "FAILED" or another default status
                break;
        }

        ordersRepository.save(order);
    }

    private void validatePaymentAmount(PaymentRequestDTO paymentRequestDTO, Orders order, Payments.PaymentMethod paymentMethod) {
        // Validate payment amount based on the payment method
        if (paymentMethod == Payments.PaymentMethod.CASH) {
            if (paymentRequestDTO.getAmount() < order.getTotalPrice()) {
                throw new IllegalArgumentException("Payment amount for CASH must be greater than or equal to order total price");
            }
        }  else if (paymentMethod == Payments.PaymentMethod.QRIS) {
            if (paymentRequestDTO.getAmount() <= 0) {
                throw new IllegalArgumentException("Payment amount for QRIS must be greater than zero");
            }
        }
    }

    @Override
    public byte[] generatedReceipt(PaymentResponseDTO paymentResponseDTO) {
        if (paymentResponseDTO == null || paymentResponseDTO.getOrder() == null || paymentResponseDTO.getOrder().getOrderDetails() == null) {
            throw new IllegalArgumentException("Invalid paymentResponseDTO or missing order details");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);

            // Set flexible height but constrain all content to fit on one page
            pdf.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(288, 432));
            Document document = new Document(pdf);
            document.setMargins(10, 10, 10, 10);

            // Store Header
            document.add(new Paragraph("Dapoer Ajendam")
                    .setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(10));
            document.add(new Paragraph("Jl. Perjuangan, Cinta Damai, Kec. Medan Helvetia,")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(7));
            document.add(new Paragraph("Kota Medan, Sumatera Utara 20123")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(7));
            document.add(new Paragraph("--------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(8));

            // Transaction Details
            document.add(new Paragraph(String.format("Date : %s", paymentResponseDTO.getPaymentDate().toLocalDate()))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Time : %s", paymentResponseDTO.getPaymentDate().toLocalTime()))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Customer : %s", paymentResponseDTO.getOrder().getCustomerName()))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Order No : %s", paymentResponseDTO.getOrder().getIdOrder()))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("--------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(8));

            // Order Items Table
            Table table = new Table(new float[]{4f, 1f, 2f});
            table.setWidth(UnitValue.createPercentValue(100));

            // Table Header
            table.addHeaderCell(createHeaderCell("Product Name"));
            table.addHeaderCell(createHeaderCell("Qty"));
            table.addHeaderCell(createHeaderCell("Subtotal"));

            // Order Details
            int totalQuantity = 0;
            for (OrderDetailResponseDTO item : paymentResponseDTO.getOrder().getOrderDetails()) {
                totalQuantity += item.getQuantity();
                table.addCell(createCell(item.getProductName()));
                table.addCell(createCell(String.valueOf(item.getQuantity())));
                table.addCell(createCell(String.format("Rp %.2f", item.getSubtotalPrice())));
            }

            document.add(table);
            document.add(new Paragraph("--------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(8));

            // Payment Summary
            double totalPrice = paymentResponseDTO.getOrder().getTotalPrice();
            double paidAmount = paymentResponseDTO.getAmount();
            double change = paidAmount - totalPrice;

            document.add(new Paragraph(String.format("Total Items: %d", totalQuantity))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Total: Rp %.2f", totalPrice))
                    .setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Paid (%s): Rp %.2f", paymentResponseDTO.getMethod(), paidAmount))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Change: Rp %.2f", change))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("--------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(8));

            // Footer
            document.add(new Paragraph("Kritik dan Saran:")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(7));
            document.add(new Paragraph("Instagram: @dapoerajendam")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(7));

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontSize(8))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(8))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payments payment = paymentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return convertToResponse(payment);
    }

    private PaymentResponseDTO convertToResponse(Payments payment) {
        return PaymentResponseDTO.builder()
                .idPayment(payment.getIdPayment())
                .amount(payment.getAmount())
                .method(String.valueOf(payment.getMethod()))
                .paymentDate(payment.getPaymentDate())
                .order(convertToResponse(payment.getOrder()))
                .qrisResponse(payment.getQrisResponse())
                .statusMidtrans(String.valueOf(payment.getStatusMidtrans()))
                .change(payment.getAmount()-payment.getOrder().getTotalPrice())
                .build();
    }

    private OrdersResponseDTO convertToResponse(Orders order) {
        return OrdersResponseDTO.builder()
                .idOrder(order.getIdOrder())
                .totalPrice(order.getTotalPrice())
                .customerName(order.getCustomerName())
                .orderDate(order.getOrderDate())
                .period(order.getPeriod())
                .years(order.getYears())
                .orderDetails(convertToResponse(order.getOrderDetails()))
                .status(String.valueOf(order.getStatus()))
                .build();
    }

    private List<OrderDetailResponseDTO> convertToResponse(List<OrderDetails> orderDetails) {
        return orderDetails.stream().map(
                orderDetail -> OrderDetailResponseDTO.builder()
                        .idOrderDetail(orderDetail.getIdOrderDetail())
                        .productName(orderDetail.getProducts().getProductName())
                        .pricePerUnit(orderDetail.getProducts().getProductPrice().doubleValue())
                        .quantity(orderDetail.getQuantity())
                        .subtotalPrice(orderDetail.getSubtotalPrice())
                        .build()
        ).collect(Collectors.toList());
    }
}
