package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.OrdersResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.PurchaseResponseDTO;
import com.abpgroup.managementsystem.service.BusinessPerformanceService;
import com.abpgroup.managementsystem.service.OrderService;
import com.abpgroup.managementsystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessPerformanceServiceImpl  implements BusinessPerformanceService {
    private final PurchaseService purchaseService;
    private final OrderService orderService;


    @Override
    public BusinessPerformanceResponseDTO getBusinessPerformanceByPeriodAndYears(String period, Long years) {
        String periodUpper = period.toUpperCase();
        List<OrdersResponseDTO> ordersResponseDTO = orderService.getOrdersByPeriodAndYears(periodUpper, years);
        PurchaseResponseDTO purchaseResponseDTO = purchaseService.getPurchasesByPeriodAndYears(periodUpper, years);

        if (ordersResponseDTO.isEmpty()) {
            double total = purchaseResponseDTO.getPurchaseTotalPrice();
            return BusinessPerformanceResponseDTO.builder()
                    .totalProfit((long) total)
                    .period(periodUpper)
                    .years(years)
                    .build();
        }

        if (purchaseResponseDTO == null || purchaseResponseDTO.getPurchaseTotalPrice() == 0.0) {
            double total = ordersResponseDTO.stream().mapToDouble(OrdersResponseDTO::getTotalPrice).sum();
            return BusinessPerformanceResponseDTO.builder()
                    .totalProfit((long) total)
                    .period(periodUpper)
                    .years(years)
                    .build();
        }

        return BusinessPerformanceResponseDTO.builder()
                .totalProfit((long) (ordersResponseDTO.stream().mapToDouble(OrdersResponseDTO::getTotalPrice).sum() - purchaseResponseDTO.getPurchaseTotalPrice()))
                .period(periodUpper)
                .years(years)
                .build();
    }
}
