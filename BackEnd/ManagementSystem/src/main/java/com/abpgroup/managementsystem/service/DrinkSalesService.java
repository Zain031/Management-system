package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.DrinkSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinkSalesResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DrinkSalesService {
    DrinkSalesResponseDTO createDrinkSales(DrinkSalesRequestDTO drinkSalesRequestDTO);
    List<DrinkSalesResponseDTO> getAllDrinkSales();
    List<DrinkSalesResponseDTO> getDrinkSalesByDate(LocalDate dateDrinkSales);
    List<DrinkSalesResponseDTO> getDrinkSalesByPeriod(String period);
    DrinkSalesResponseDTO updateDrinkSales(Long id, DrinkSalesRequestDTO drinkSalesRequestDTO);
    DrinkSalesResponseDTO deleteDrinkSales(Long id);

}
