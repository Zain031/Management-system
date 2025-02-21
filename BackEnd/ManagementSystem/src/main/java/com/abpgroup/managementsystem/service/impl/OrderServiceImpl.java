package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.OrdersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.OrderDetailResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.OrdersResponseDTO;
import com.abpgroup.managementsystem.model.entity.OrderDetails;
import com.abpgroup.managementsystem.model.entity.Orders;
import com.abpgroup.managementsystem.model.entity.Payments;
import com.abpgroup.managementsystem.model.entity.Products;
import com.abpgroup.managementsystem.repository.OrderDetailsRepository;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.repository.PaymentsRepository;
import com.abpgroup.managementsystem.repository.ProductsRepository;
import com.abpgroup.managementsystem.service.MidtransService;
import com.abpgroup.managementsystem.service.OrderService;
import com.abpgroup.managementsystem.utils.CapitalizeFirstLetter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductsRepository productsRepository;
    private final PaymentsRepository paymentsRepository;
    private final MidtransService midtransService;
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrdersResponseDTO createOrder(OrdersRequestDTO ordersRequestDTO) {
        // Membuat entitas Order
        Orders order = Orders.builder()
                .customerName(CapitalizeFirstLetter.capitalizeFirstLetter(ordersRequestDTO.getCustomerName()))
                .orderDate(LocalDateTime.now())
                .status(Orders.OrderStatus.valueOf("PENDING"))
                .totalPrice(0.0)
                .period(String.valueOf(LocalDateTime.now().getMonth()))
                .years((long) LocalDateTime.now().getYear())
                .build();

        // Simpan order ke database
        Orders savedOrder = ordersRepository.save(order);

        // Memproses OrderDetails
        List<OrderDetails> orderDetails = ordersRequestDTO.getOrderDetailsRequestDTOList().stream().map(detailRequest -> {
            Products menu = productsRepository.findById((detailRequest.getIdProduct()))
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + detailRequest.getIdProduct()));

            return OrderDetails.builder()
                    .order(savedOrder)
                    .products(menu)
                    .quantity(detailRequest.getQuantity())
                    .pricePerUnit(Double.valueOf(menu.getProductPrice()))
                    .subtotalPrice((double) (menu.getProductPrice() * detailRequest.getQuantity()))
                    .build();
        }).collect(Collectors.toList());

        // Simpan OrderDetails ke database
        orderDetailsRepository.saveAll(orderDetails);

        // Hitung total harga pesanan
        double totalPrice = orderDetails.stream()
                .mapToDouble(OrderDetails::getSubtotalPrice)
                .sum();

        savedOrder.setTotalPrice(totalPrice);
        ordersRepository.save(savedOrder);

        // Mengonversi ke DTO untuk response
        return convertToResponse(savedOrder, orderDetails);
    }

    @Override
    public OrdersResponseDTO getOrderById(String id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderIdOrder(id);
        return convertToResponse(order, orderDetails);
    }


    @Override
    public Page<OrdersResponseDTO> getAllOrders(Pageable pageable) {
        Page<Orders> orders = ordersRepository.findAll(pageable);
        return orders.map(
                order -> convertToResponse(order, orderDetailsRepository.findByOrderIdOrder(order.getIdOrder()))
        );
    }

    @Override
    public List<OrdersResponseDTO> getOrdersByDateOrders(LocalDate dateOrders) {
        List<Orders> ordersList = ordersRepository.getOrdersByDatePurchases((dateOrders));
        if (ordersList.isEmpty()) {
            return Collections.emptyList();
        }
        return ordersList.stream().map(order -> {
            List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderIdOrder(order.getIdOrder());
            return convertToResponse(order, orderDetails != null ? orderDetails : Collections.emptyList());
        }).collect(Collectors.toList());
    }

    @Override
    public byte[] generatedPdfByDateOrders(List<OrdersResponseDTO> ordersResponseDTO, LocalDate dateOrders) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Set page size to A4
            pdf.setDefaultPageSize(PageSize.A4);

            // Add title
            Paragraph title = new Paragraph("Orders Report by Date")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // Add orders date
            document.add(new Paragraph("Orders Date: " + dateOrders)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            document.add(new Paragraph(" "));

            // Define column widths for the table
            float[] columnWidths = {1f, 3f, 2f, 2f, 2f};

            // Total price across all orders
            double grandTotal = 0;

            int orderIndex = 1;
            // Loop through all orders and add them to the document
            for (OrdersResponseDTO orders : ordersResponseDTO) {
                // Add each order's title
                Paragraph orderTitle = new Paragraph("Order " + orderIndex++ + " - Order ID: " + orders.getIdOrder())
                        .setBold()
                        .setFontSize(14)
                        .setMarginBottom(5);
                document.add(orderTitle);

                // Add customer and order details
                document.add(new Paragraph("Customer: " + orders.getCustomerName())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));
                document.add(new Paragraph("Order Date: " + orders.getOrderDate().toLocalDate())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));
                document.add(new Paragraph("Order Status: " + orders.getStatus())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));

                // Format order total price to Rupiah currency format
                String totalPriceFormatted = String.format("Rp %.2f", orders.getTotalPrice());
                document.add(new Paragraph("Order Total: " + totalPriceFormatted)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setMarginBottom(10));

                // Add table for order details
                Table table = new Table(columnWidths);
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Price Per Unit").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Subtotal Price").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Add rows for each order detail
                int index = 1;
                for (OrderDetailResponseDTO detail : orders.getOrderDetails()) {
                    String pricePerUnitFormatted = String.format("Rp %.2f", detail.getPricePerUnit());
                    String subtotalPriceFormatted = String.format("Rp %.2f", detail.getSubtotalPrice());

                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(detail.getProductName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getQuantity()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(pricePerUnitFormatted)).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(subtotalPriceFormatted)).setTextAlignment(TextAlignment.CENTER));

                    // Add subtotal price to grand total
                    grandTotal += detail.getSubtotalPrice();
                }

                // Add the table to the document
                document.add(table);

                // Add some space after each order
                document.add(new Paragraph(" "));

                // Add a separator between orders (optional)
                document.add(new Paragraph("-----------------------------------------------------")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10)
                        .setMarginBottom(10));
            }

            // Add the grand total price section
            String grandTotalFormatted = String.format("Rp %.2f", grandTotal);
            Paragraph grandTotalParagraph = new Paragraph("Grand Total Price: " + grandTotalFormatted)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(grandTotalParagraph);

            // Close the document
            document.close();

            // Return the PDF byte array
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Orders Report PDF by Date", e);
        }
    }


    @Override
    public byte[] generatedPdfByPeriodAndYears(List<OrdersResponseDTO> ordersResponseDTO, String periodUpper, Long years) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Set page size to A4
            pdf.setDefaultPageSize(PageSize.A4);

            // Add title
            Paragraph title = new Paragraph("Order Report")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // Add period and year
            document.add(new Paragraph("Period: " + periodUpper + "\nYear: " + years)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));

            document.add(new Paragraph(" "));

            // Define column widths for the table
            float[] columnWidths = {1f, 3f, 2f, 2f, 2f};

            // Total price across all orders
            double grandTotal = 0;

            int orderIndex = 1;
            // Loop through all orders and add them to the document
            for (OrdersResponseDTO orders : ordersResponseDTO) {
                // Add order header
                Paragraph orderTitle = new Paragraph("Order " + orderIndex++ + " - Order ID: " + orders.getIdOrder())
                        .setBold()
                        .setFontSize(14)
                        .setMarginBottom(5);
                document.add(orderTitle);

                // Add order details
                document.add(new Paragraph("Customer: " + orders.getCustomerName())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));
                document.add(new Paragraph("Order Date: " + orders.getOrderDate().toLocalDate())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));
                document.add(new Paragraph("Order Status: " + orders.getStatus())
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT));

                // Format order total price to Rupiah currency format
                String totalPriceFormatted = String.format("Rp %.2f", orders.getTotalPrice());
                document.add(new Paragraph("Order Total: " + totalPriceFormatted)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setMarginBottom(10));

                // Create table for order details
                Table table = new Table(columnWidths);
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Price Per Unit").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Subtotal Price").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Add rows for each order detail
                int index = 1;
                for (OrderDetailResponseDTO detail : orders.getOrderDetails()) {
                    String pricePerUnitFormatted = String.format("Rp %.2f", detail.getPricePerUnit());
                    String subtotalPriceFormatted = String.format("Rp %.2f", detail.getSubtotalPrice());

                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(detail.getProductName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getQuantity()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(pricePerUnitFormatted)).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(subtotalPriceFormatted)).setTextAlignment(TextAlignment.CENTER));

                    // Add subtotal price to grand total
                    grandTotal += detail.getSubtotalPrice();
                }

                // Add table to document
                document.add(table);

                // Add some space after each order
                document.add(new Paragraph(" "));

                // Add a separator between orders (optional)
                document.add(new Paragraph("-----------------------------------------------------")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10)
                        .setMarginBottom(10));
            }

            // Add the grand total price section
            String grandTotalFormatted = String.format("Rp %.2f", grandTotal);
            Paragraph grandTotalParagraph = new Paragraph("Grand Total Price: " + grandTotalFormatted)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(grandTotalParagraph);

            // Close the document
            document.close();

            // Return the PDF byte array
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Order Report PDF by Period and Year", e);
        }
    }

    @Override
    public Page<OrdersResponseDTO> getOrdersByPeriodYearsAndStatus(
            String periodUpper,
            Long years,
            String status,
            Pageable pageable) {

        Page<Orders> orders;

        // Ambil data order berdasarkan status jika disediakan
        if (status == null || status.isEmpty()) {
            orders = ordersRepository.getOrdersAllByPeriodAndYears(periodUpper.toUpperCase(), years, pageable);
        } else {
            Orders.OrderStatus orderStatus;
            try {
                orderStatus = Orders.OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid order status: " + status);
            }
            orders = ordersRepository.getOrdersByPeriodYearsAndStatus(
                    periodUpper.toUpperCase(), years, orderStatus, pageable);
        }

        // Ambil orderIds untuk transaksi terkait
        List<String> orderIds = orders.getContent().stream()
                .map(Orders::getIdOrder)
                .collect(Collectors.toList());

        // Ambil status transaksi dari Midtrans secara paralel untuk efisiensi
        Map<String, String> midtransTransactionStatuses = orderIds.stream()
                .collect(Collectors.toMap(orderId -> orderId, orderId -> midtransService.getTransactionStatus(orderId)));

        // Ambil semua metode pembayaran terkait dengan orderIds
        Map<String, Payments> paymentMap = paymentsRepository.findMethodByOrderId(orderIds).stream()
                .collect(Collectors.toMap(payment -> payment.getOrder().getIdOrder(), payment -> payment));

        // Perbarui setiap order berdasarkan data pembayaran dan status Midtrans
        orders.getContent().forEach(order -> {
            Payments payment = paymentMap.get(order.getIdOrder());
            if (payment != null) {
                order.setLinkQris(payment.getQrisResponse()); // Set QRIS response
                order.setMethod(String.valueOf(payment.getMethod())); // Set metode pembayaran

                // Jika metode pembayaran "CASH", set status transaksi menjadi "settlement"
                if (payment.getMethod() == Payments.PaymentMethod.CASH) {
                    midtransTransactionStatuses.put(order.getIdOrder(), "settlement");
                }
            }

            // Perbarui status order berdasarkan status transaksi Midtrans
            updateOrderStatusBasedOnMidtrans(order, midtransTransactionStatuses.get(order.getIdOrder()));
        });

        // Simpan perubahan order yang diperbarui
        ordersRepository.saveAll(orders.getContent());

        // Konversi entitas ke DTO dan kembalikan hasil
        return orders.map(order -> convertToResponse(order, order.getOrderDetails()));
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

        // Menyimpan perubahan status ke database
        ordersRepository.saveAndFlush(order);
        logger.info("Order status updated to {} for orderId {}", order.getStatus(), order.getIdOrder());
    }

    @Override
    public List<OrdersResponseDTO> getOrdersByPeriodAndYears(String upperCase, long year) {
        List<Orders> orders = ordersRepository.getOrdersByPeriodAndYears(upperCase.toUpperCase(), year);
        return orders.stream().map(order -> convertToResponse(order, order.getOrderDetails())).collect(Collectors.toList());
    }

    private OrdersResponseDTO convertToResponse(Orders orders, List<OrderDetails> orderDetails) {
        List<OrderDetailResponseDTO> orderDetailResponseDTOs = orderDetails.stream()
                .map(detail -> OrderDetailResponseDTO.builder()
                        .idOrderDetail(detail.getIdOrderDetail())
                        .productName(detail.getProducts().getProductName())
                        .quantity(detail.getQuantity())
                        .pricePerUnit(detail.getPricePerUnit())
                        .subtotalPrice(detail.getSubtotalPrice())
                        .build())
                .collect(Collectors.toList());


        return OrdersResponseDTO.builder()
                .idOrder(orders.getIdOrder())
                .customerName(orders.getCustomerName())
                .orderDate(orders.getOrderDate())
                .status(String.valueOf(orders.getStatus()))
                .totalPrice(orders.getTotalPrice())
                .orderDetails(orderDetailResponseDTOs)
                .period(orders.getPeriod())
                .years(orders.getYears())
                .linkQris(orders.getLinkQris())
                .build();
    }

}