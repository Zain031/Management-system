package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.DrinksRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinksResponseDTO;

import java.util.List;

public interface DrinksService {
    DrinksResponseDTO createDrinks(DrinksRequestDTO drinksRequestDTO);
    List<DrinksResponseDTO> getAllDrinks();
    DrinksResponseDTO getDrinksById(Long id);
    DrinksResponseDTO updateDrinks(Long id,DrinksRequestDTO drinksRequestDTO);
    DrinksResponseDTO deleteDrinks(Long id);
}
