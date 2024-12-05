package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.PurchaseResponseDTO;
import com.abpgroup.managementsystem.model.entity.Inventory;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final InventoryRepository inventoryRepository;

    @Override
    public PurchaseResponseDTO getPurchasesByDate(LocalDate datePurchases) {
        List<Inventory> inventoryList = inventoryRepository.findInventoryByDatePurchases(datePurchases);
        if (inventoryList.isEmpty()) {
            return PurchaseResponseDTO.builder()
                    .purchaseTotalQuantity(0.0)
                    .purchaseTotalPrice(0.0)
                    .datePurchase(datePurchases)
                    .period(datePurchases.getMonth().name())
                    .years(Long.valueOf(datePurchases.getYear()))
                    .build();
        }
        Double totalPurchases= inventoryRepository.calculateTotalPurchasesByDate(datePurchases);
        Double totalPurchasesPrice = inventoryRepository.calculateTotalPurchasesPriceByDate(datePurchases);

        return PurchaseResponseDTO.builder()
                .purchaseTotalQuantity(totalPurchases)
                .purchaseTotalPrice(totalPurchasesPrice)
                .datePurchase(datePurchases)
                .period(datePurchases.getMonth().name())
                .years(Long.valueOf(datePurchases.getYear()))
                .build();
    }

    @Override
    public PurchaseResponseDTO getPurchasesByPeriodAndYears(String period, Long years) {
        String periodUpper = period.toUpperCase();
        List<Inventory> inventoryList = inventoryRepository.findInventoryByPeriodAndYears(periodUpper, years);
        if (inventoryList.isEmpty()) {
            return PurchaseResponseDTO.builder()
                    .purchaseTotalQuantity(0.0)
                    .purchaseTotalPrice(0.0)
                    .period(periodUpper)
                    .years(years)
                    .build();
        }
        Double totalPurchases = inventoryRepository.calculateTotalPurchasesByPeriod(periodUpper);
        Double totalPurchasesPrice = inventoryRepository.calculateTotalPurchasesPriceByPeriod(periodUpper);

        return PurchaseResponseDTO.builder()
                .purchaseTotalQuantity(totalPurchases)
                .purchaseTotalPrice(totalPurchasesPrice)
                .period(periodUpper)
                .years(years)
                .build();
    }

}
