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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public PaymentResponseDTO processPayment(String orderId, PaymentRequestDTO paymentRequestDTO) {
        // Retrieve order by ID and validate its existence
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Validate order status
        if (order.getStatus() == Orders.OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Order already completed with ID: " + orderId);
        }

        try {
            // Parse payment method and amount
            Payments.PaymentMethod paymentMethod = Payments.PaymentMethod.valueOf(paymentRequestDTO.getPaymentMethod());
            BigDecimal amount = BigDecimal.valueOf(paymentRequestDTO.getAmount());
            BigDecimal totalPrice = BigDecimal.valueOf(order.getTotalPrice());

            // Validate payment amount
            validatePaymentAmount(paymentRequestDTO, order, paymentMethod);

            // Process payment based on payment method
            Payments payment = processPaymentByMethod(order, paymentMethod, amount, totalPrice);

            // Save payment and update order status
            paymentsRepository.save(payment);
            return convertToResponse(payment);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error for orderId: {}. Error: {}", orderId, e.getMessage());
            throw e;

        } catch (Exception e) {
            logger.error("Failed to process payment for orderId: {}. Error: {}", orderId, e.getMessage(), e);

            // Cancel the order if payment fails
            order.setStatus(Orders.OrderStatus.CANCELED);
            ordersRepository.save(order);

            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<PaymentResponseDTO> getAllPayments(Pageable pageable) {
        Page<Payments> payments = paymentsRepository.findAllOrderByDatePaymentNow(pageable);

        // Use a Set to keep track of unique Order IDs (as String)
        Set<String> uniqueOrderIds = new HashSet<>();

        for (Payments payment : payments.getContent()) {
            // Ensure that we only process unique Order IDs
            String orderId = payment.getOrder() != null ? payment.getOrder().getIdOrder() : null;

            if (orderId != null && !uniqueOrderIds.contains(orderId)) {
                uniqueOrderIds.add(orderId); // Add to set to track uniqueness
                System.out.println("Order ID: " + orderId);  // Log the unique Order ID
            }

            String midtransTransactionResponse = "";

            // If payment method is CASH, set midtransTransactionResponse to "settlement"
            if (payment.getMethod() == Payments.PaymentMethod.CASH) {
                midtransTransactionResponse = "settlement";
            } else if (payment.getMethod() == Payments.PaymentMethod.QRIS) {
                // If payment method is QRIS, set midtransTransactionResponse to the transaction status
                if (payment.getOrder() != null && payment.getOrder().getIdOrder() != null) {
                    midtransTransactionResponse = midtransService.getTransactionStatus(payment.getOrder().getIdOrder());
                    updateOrderStatusBasedOnMidtrans(payment.getOrder(), midtransTransactionResponse);
                }
            }

            // Set the midtrans status response
            payment.setStatusMidtrans(midtransTransactionResponse);
        }

        // Save updated payments with statusMidtrans and persist them to the database
        paymentsRepository.saveAll(payments.getContent());

        return payments.map(this::convertToResponse);
    }


    private Payments processPaymentByMethod(Orders order, Payments.PaymentMethod paymentMethod, BigDecimal amount, BigDecimal totalPrice) {
        switch (paymentMethod) {
            case QRIS:
                return processQrisPayment(order, amount, totalPrice);
            case CASH:
                return processCashPayment(order, amount, totalPrice);
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
    }

    private Payments processQrisPayment(Orders order, BigDecimal amount, BigDecimal totalPrice) {
        if (amount.compareTo(totalPrice) != 0) {
            throw new IllegalArgumentException("QRIS payment amount must match the total order price.");
        }

        logger.info("Processing QRIS payment for orderId: {}", order.getIdOrder());

        BigDecimal amountFee = amount.multiply(BigDecimal.valueOf(0.0075));  // 0.75% fee
        BigDecimal totalAmount = amount.add(amountFee);

        String qrisTransactionResponse = midtransService.createQrisTransaction(String.valueOf(order.getIdOrder()), totalAmount.longValue());

        return Payments.builder()
                .order(order)
                .amount(amount.doubleValue())
                .method(Payments.PaymentMethod.QRIS)
                .paymentDate(LocalDateTime.now())
                .qrisResponse(qrisTransactionResponse)
                .statusMidtrans("pending")
                .build();
    }

    private Payments processCashPayment(Orders order, BigDecimal amount, BigDecimal totalPrice) {
        // Validate payment amount
        if (amount.compareTo(totalPrice) < 0) {
            throw new IllegalArgumentException("CASH payment amount cannot be less than the total order price.");
        }

        logger.info("Processing CASH payment for orderId: {}", order.getIdOrder());

        // Simulate response for CASH payment
        String midtransTransactionResponse = "settlement";

        // Check response status and update order status
        order.setStatus(Orders.OrderStatus.COMPLETED);
        logger.info("Payment succeeded, orderId: {} marked as COMPLETED", order.getIdOrder());

        // Save order status to database
        ordersRepository.save(order);

        return Payments.builder()
                .order(order)
                .amount(amount.doubleValue())
                .method(Payments.PaymentMethod.CASH)
                .paymentDate(LocalDateTime.now())
                .statusMidtrans(midtransTransactionResponse)
                .build();
    }


    private void validatePaymentAmount(PaymentRequestDTO paymentRequestDTO, Orders order, Payments.PaymentMethod paymentMethod) {
        if (paymentMethod == Payments.PaymentMethod.CASH && paymentRequestDTO.getAmount() < order.getTotalPrice()) {
            throw new IllegalArgumentException("Payment amount for CASH must be greater than or equal to order total price.");
        } else if (paymentMethod == Payments.PaymentMethod.QRIS && paymentRequestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount for QRIS must be greater than zero.");
        }
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
                logger.warn("Unknown transaction status received for orderId {}: {}", order.getIdOrder(), midtransTransactionResponse);
                order.setStatus(Orders.OrderStatus.CANCELED);
                break;
        }

        ordersRepository.saveAndFlush(order);
        logger.info("Order status updated to {} for orderId {}", order.getStatus(), order.getIdOrder());
    }

    public byte[] generatedReceipt(PaymentResponseDTO paymentResponseDTO) {
        if (paymentResponseDTO == null || paymentResponseDTO.getOrder() == null || paymentResponseDTO.getOrder().getOrderDetails() == null) {
            throw new IllegalArgumentException("Invalid paymentResponseDTO or missing order details");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Generate PDF
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(288, 432));
            Document document = new Document(pdf);
            document.setMargins(10, 10, 10, 10);

            // Header
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
            double adminFee = 0.0;
            double grandTotal = 0.0;
            double paidAmount = 0.0;
            double change = 0.0;

            // Determine admin fee based on payment method
            if (!"CASH".equalsIgnoreCase(paymentResponseDTO.getMethod())) {
                adminFee = totalPrice * 0.0075; // Admin fee 0.75% for non-cash
                grandTotal = totalPrice + adminFee;
                paidAmount=grandTotal;
            }

            if ("CASH".equalsIgnoreCase(paymentResponseDTO.getMethod())) {
                paidAmount = paymentResponseDTO.getAmount();
                grandTotal = totalPrice;
                change = paidAmount - grandTotal;
            }

            // Display Summary
            document.add(new Paragraph(String.format("Total Items: %d", totalQuantity))
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(String.format("Total: Rp %.2f", totalPrice))
                    .setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER));

            if (adminFee > 0) {
                document.add(new Paragraph(String.format("Admin Fee (0.75%%): Rp %.2f", adminFee))
                        .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            } else {
                document.add(new Paragraph("Admin Fee: Rp 0 (Cash Payment)")
                        .setFontSize(8).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(new Paragraph(String.format("Grand Total: Rp %.2f", grandTotal))
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

            byte[] pdfBytes = outputStream.toByteArray();

            // Send PDF to Printer
//        sendToPrinter(pdfBytes);

            return pdfBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error generating or printing PDF", e);
        }
    }


//    private void sendToPrinter(byte[] pdfBytes) {
//        try {
//            // Locate default print service
//            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//            if (printService == null) {
//                throw new RuntimeException("No default print service found");
//            }
//
//            // Create a print job
//            DocPrintJob printJob = printService.createPrintJob();
//            Doc pdfDoc = new SimpleDoc(new ByteArrayInputStream(pdfBytes), DocFlavor.INPUT_STREAM.AUTOSENSE, null);
//
//            // Print the document
//            printJob.print(pdfDoc, null);
//        } catch (Exception e) {
//            throw new RuntimeException("Error sending PDF to printer", e);
//        }
//    }


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
    public PaymentResponseDTO getPaymentById(String id, Orders order) {
        Payments payment = paymentsRepository.getPaymentsByOrderId(order.getIdOrder());
        return convertToResponse(payment);
    }


    private PaymentResponseDTO convertToResponse(Payments payment) {
        return PaymentResponseDTO.builder()
                .idPayment(payment.getIdPayment())
                .amount(payment.getAmount())
                .method(String.valueOf(payment.getMethod()))
                .paymentDate(payment.getPaymentDate())
                .order(convertToResponse(payment.getOrder()))
                .linkQris(payment.getQrisResponse())
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
