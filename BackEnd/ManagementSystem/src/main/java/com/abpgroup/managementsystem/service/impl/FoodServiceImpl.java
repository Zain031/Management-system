package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.FoodsRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodsResponseDTO;
import com.abpgroup.managementsystem.model.entity.Foods;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.FoodsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodsRepository foodsRepository;
    private final UsersRepository usersRepository;
    @Override
    public FoodsResponseDTO createFoods(FoodsRequestDTO foodsRequestDTO) {
        if (foodsRequestDTO.getFoodName() == null || foodsRequestDTO.getFoodName().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty");
        }

        if (foodsRequestDTO.getFoodPrice() <= 0) {
            throw new IllegalArgumentException("Food price must be greater than zero");
        }

        if (foodsRequestDTO.getTotalFood() <= 0) {
            throw new IllegalArgumentException("Total food quantity must be greater than zero");
        }

        Users user = usersRepository.findById(foodsRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Foods foods = Foods.builder()
                .user(user)
                .foodName(foodsRequestDTO.getFoodName())
                .foodPrice(foodsRequestDTO.getFoodPrice())
                .totalFood(foodsRequestDTO.getTotalFood())
                .totalFoodPrice(foodsRequestDTO.getFoodPrice() * foodsRequestDTO.getTotalFood())
                .build();
        foodsRepository.save(foods);
        return convertToResponse(foods);
    }

    @Override
    public List<FoodsResponseDTO> getAllFoods() {
        return  foodsRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public FoodsResponseDTO getFoodsById(Long id) {
        return foodsRepository.findById(id).map(this::convertToResponse).orElse(null);
    }

    @Override
    public FoodsResponseDTO updateFoods(Long id, FoodsRequestDTO foodsRequestDTO) {
        Foods foods = foodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foods not found"));
        foods.setFoodName(foodsRequestDTO.getFoodName());
        foods.setFoodPrice(foodsRequestDTO.getFoodPrice());
        foods.setTotalFood(foodsRequestDTO.getTotalFood());
        foods.setTotalFoodPrice(foodsRequestDTO.getFoodPrice() * foodsRequestDTO.getTotalFood());
        foodsRepository.save(foods);
        return convertToResponse(foods);
    }

    @Override
    public FoodsResponseDTO deleteFoods(Long id) {
        Foods foods = foodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foods not found"));
        foodsRepository.delete(foods);
        return convertToResponse(foods);
    }

    private FoodsResponseDTO convertToResponse(Foods foods) {
        return FoodsResponseDTO.builder()
                .idFood(foods.getIdFood())
                .foodName(foods.getFoodName())
                .foodPrice(foods.getFoodPrice())
                .totalFood(foods.getTotalFood())
                .totalFoodPrice(foods.getTotalFoodPrice())
                .build();
    }
}
