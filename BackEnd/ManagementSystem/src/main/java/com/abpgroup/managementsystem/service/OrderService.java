package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.OrdersRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.OrdersResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrdersResponseDTO createOrder(OrdersRequestDTO ordersRequestDTO);
    OrdersResponseDTO getOrderById(String id);
    Page<OrdersResponseDTO> getAllOrders(Pageable pageable);
    Page<OrdersResponseDTO> getOrdersByPeriodYearsAndStatus(String periodUpper, Long years, String status, Pageable pageable);
    List<OrdersResponseDTO> getOrdersByDateOrders(LocalDate dateOrders);
    byte[] generatedPdfByDateOrders(List<OrdersResponseDTO> ordersResponseDTO, LocalDate dateOrders);
    byte[] generatedPdfByPeriodAndYears(List<OrdersResponseDTO> ordersResponseDTO, String periodUpper, Long years);

    List<OrdersResponseDTO> getOrdersByPeriodAndYears(String upperCase, long year);
}
