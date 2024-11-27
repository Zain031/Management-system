package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.PurchaseResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;
import com.abpgroup.managementsystem.service.BusinessPerfomanceService;
import com.abpgroup.managementsystem.service.PurchaseService;
import com.abpgroup.managementsystem.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BusinessPerfomanceServiceImpl implements BusinessPerfomanceService {
    private final PurchaseService purchaseService;
    private final SalesService salesService;


    @Override
    public BusinessPerformanceResponseDTO getBusinessPerformanceByPeriodAndYears(String period, Long years) {
        String periodUpper = period.toUpperCase();
        SalesResponseDTO salesResponseDTO = salesService.getSalesByPeriodAndYears(periodUpper, years);
        PurchaseResponseDTO purchaseResponseDTO = purchaseService.getPurchasesByPeriodAndYears(periodUpper, years);

        return BusinessPerformanceResponseDTO.builder()
                .totalProfit((long) (salesResponseDTO.getTotalSalesPrice() - purchaseResponseDTO.getPurchaseTotalPrice()))
                .totalLostProfit(salesResponseDTO.getTotalLeftoverSalesPrice())
                .period(periodUpper)
                .years(years)
                .build();
    }
}
