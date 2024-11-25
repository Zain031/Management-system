package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.entity.Sales;
import com.abpgroup.managementsystem.repository.ProductSalesRepository;
import com.abpgroup.managementsystem.repository.SalesRepository;
import com.abpgroup.managementsystem.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final ProductSalesRepository productSalesRepository;
    private final SalesRepository salesRepository;

    @Override
    public SalesResponseDTO getSalesByDate(LocalDate date) {

        List<ProductSales> productSalesList = productSalesRepository.findProductSalesByDateProductSales(date);
        Long totalSales = productSalesList.stream().map(ProductSales::getTotalProductSales).reduce(0L, Long::sum);
        Long totalLeftoverSales = productSalesList.stream().map(ProductSales::getLeftoverProductSales).reduce(0L, Long::sum);
        Long totalSalesPrice = productSalesList.stream().map(ProductSales::getTotalProductSalesPrice).reduce(0L, Long::sum);
        Long totalLeftoverSalesPrice = productSalesList.stream().map(ProductSales::getTotalLeftoverProductSalesPrice).reduce(0L, Long::sum);

        Sales sales = Sales.builder()
                .period(date.getMonth().name())
                .dateSales(date)
                .totalSales(totalSales)
                .totalLeftoverSales(totalLeftoverSales)
                .totalSalesPrice(totalSalesPrice)
                .totalLeftoverSalesPrice(totalLeftoverSalesPrice)
                .build();

        salesRepository.save(sales);

        return toSalesResponseDTO(sales);

    }

    @Override
    public SalesResponseDTO getSalesByPeriod(String period) {
        String periodUpper = period.toUpperCase();
        Long totalSalesPriceByPeriod = salesRepository.calculateTotalSalesPriceByPeriod(periodUpper);
        Long totalLeftoverSalesPriceByPeriod = salesRepository.calculateTotalLeftoverSalesPriceByPeriod(periodUpper);
        Long totalSalesByPeriod = salesRepository.calculateTotalSalesByPeriod(periodUpper);
        Long totalLeftoverSalesByPeriod = salesRepository.calculateTotalLeftoverSalesByPeriod(periodUpper);

        return SalesResponseDTO.builder()
                .totalSalesPrice(totalSalesPriceByPeriod)
                .totalLeftoverSalesPrice(totalLeftoverSalesPriceByPeriod)
                .totalSales(totalSalesByPeriod)
                .totalLeftoverSales(totalLeftoverSalesByPeriod)
                .period(periodUpper)
                .build();
    }

    private SalesResponseDTO toSalesResponseDTO(Sales sales) {
        return SalesResponseDTO.builder()
                .idSales(sales.getIdSales())
                .totalSales(sales.getTotalSales())
                .totalLeftoverSales(sales.getTotalLeftoverSales())
                .totalSalesPrice(sales.getTotalSalesPrice())
                .totalLeftoverSalesPrice(sales.getTotalLeftoverSalesPrice())
                .dateSales(sales.getDateSales())
                .period(sales.getPeriod())
                .build();
    }
}
