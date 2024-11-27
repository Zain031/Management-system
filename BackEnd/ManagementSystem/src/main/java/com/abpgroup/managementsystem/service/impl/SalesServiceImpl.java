package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.repository.ProductSalesRepository;
import com.abpgroup.managementsystem.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final ProductSalesRepository productSalesRepository;

    @Override
    public SalesResponseDTO getSalesByDate(LocalDate date) {
        List<ProductSales> productSalesList = productSalesRepository.findProductSalesByDateProductSales(date);
        if (productSalesList.isEmpty()) {
            throw new IllegalArgumentException("No data found");
        }
        Long totalSales = productSalesList.stream().map(ProductSales::getTotalProductSales).reduce(0L, Long::sum);
        Long totalLeftoverSales = productSalesList.stream().map(ProductSales::getLeftoverProductSales).reduce(0L, Long::sum);
        Long totalSalesPrice = productSalesList.stream().map(ProductSales::getTotalProductSalesPrice).reduce(0L, Long::sum);
        Long totalLeftoverSalesPrice = productSalesList.stream().map(ProductSales::getTotalLeftoverProductSalesPrice).reduce(0L, Long::sum);

        return SalesResponseDTO.builder()
                .dateSales(date)
                .totalSales(totalSales)
                .totalLeftoverSales(totalLeftoverSales)
                .totalSalesPrice(totalSalesPrice)
                .totalLeftoverSalesPrice(totalLeftoverSalesPrice)
                .period(date.getMonth().name())
                .years(Long.valueOf(date.getYear()))
                .build();

    }

    @Override
    public SalesResponseDTO getSalesByPeriodAndYears(String period, Long years) {
        String periodUpper = period.toUpperCase();
        List<ProductSales> productSalesList = productSalesRepository.getProductSalesByPeriodAndYears(periodUpper, years);
        if (productSalesList.isEmpty()) {
            throw new IllegalArgumentException("No data found");
        }
        Long totalSalesPriceByPeriod = productSalesRepository.calculateTotalSalesPriceByPeriod(periodUpper);
        Long totalLeftoverSalesPriceByPeriod = productSalesRepository.calculateTotalLeftoverSalesPriceByPeriod(periodUpper);
        Long totalSalesByPeriod = productSalesRepository.calculateTotalSalesByPeriod(periodUpper);
        Long totalLeftoverSalesByPeriod = productSalesRepository.calculateTotalLeftoverSalesByPeriod(periodUpper);

        return SalesResponseDTO.builder()
                .period(periodUpper)
                .totalSales(totalSalesByPeriod)
                .totalLeftoverSales(totalLeftoverSalesByPeriod)
                .totalSalesPrice(totalSalesPriceByPeriod)
                .totalLeftoverSalesPrice(totalLeftoverSalesPriceByPeriod)
                .years(years)
                .build();
    }
}
