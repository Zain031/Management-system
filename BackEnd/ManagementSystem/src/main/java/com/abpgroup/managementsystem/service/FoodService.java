package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.FoodsRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodsResponseDTO;

import java.util.List;

public interface FoodService {
    FoodsResponseDTO createFoods(FoodsRequestDTO foodsRequestDTO);
    List<FoodsResponseDTO> getAllFoods();
    FoodsResponseDTO getFoodsById(Long id);
    FoodsResponseDTO updateFoods(Long id,FoodsRequestDTO foodsRequestDTO);
    FoodsResponseDTO deleteFoods(Long id);
}
