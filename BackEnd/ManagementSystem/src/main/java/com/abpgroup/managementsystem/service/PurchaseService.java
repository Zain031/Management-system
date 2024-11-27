package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.response.PurchaseResponseDTO;

import java.time.LocalDate;

public interface PurchaseService {
    PurchaseResponseDTO getPurchasesByDate(LocalDate datePurchases);

    PurchaseResponseDTO getPurchasesByPeriodAndYears(String period, Long years);
}
