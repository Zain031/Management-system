package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.FoodSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodSalesResponseDTO;

import java.time.LocalDate;
import java.util.List;


public interface FoodSalesService {
    FoodSalesResponseDTO createFoodSales(FoodSalesRequestDTO foodSalesRequestDTO);
    List<FoodSalesResponseDTO> getAllFoodSales();
    List<FoodSalesResponseDTO> getFoodSalesByDate(LocalDate dateFoodSales);
    List<FoodSalesResponseDTO> getFoodSalesByPeriod(String period);
    FoodSalesResponseDTO updateFoodSales(Long id,FoodSalesRequestDTO foodSalesRequestDTO);
    FoodSalesResponseDTO deleteFoodSales(Long id);
}
