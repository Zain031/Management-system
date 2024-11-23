package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.mapper.FoodSalesMapper;
import com.abpgroup.managementsystem.model.dto.request.FoodSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodsResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.FoodSales;
import com.abpgroup.managementsystem.model.entity.Foods;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.FoodSalesRepository;
import com.abpgroup.managementsystem.repository.FoodsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.FoodSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodSalesServiceImpl implements FoodSalesService {
    private final FoodSalesRepository foodSalesRepository;
    private final UsersRepository usersRepository;
    private final FoodsRepository foodsRepository;

    @Override
    public FoodSalesResponseDTO createFoodSales(FoodSalesRequestDTO foodSalesRequestDTO) {
        Foods foods = foodsRepository.findById(foodSalesRequestDTO.getIdFood())
                .orElseThrow(() -> new IllegalArgumentException("Foods not found"));
        Users users = usersRepository.findById(foodSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        LocalDate dateFoodSales = foodSalesRequestDTO.getDateFoodSales();

        FoodSales foodSales = FoodSales.builder()
                .user(users)
                .foods(foods)
                .totalFoodSales(foodSalesRequestDTO.getTotalFoodSales())
                .leftoverFoodSales(foodSalesRequestDTO.getTotalFoodSales())
                .totalSalesFoodPrice((foods.getFoodPrice()* foodSalesRequestDTO.getTotalFoodSales())- (foods.getFoodPrice()* foodSalesRequestDTO.getLeftoverFoodSales()))
                .dateFoodSales(foodSalesRequestDTO.getDateFoodSales())
                .period(dateFoodSales.getMonth().toString())
                .build();
        foodSalesRepository.save(foodSales);
        return convertToResponse(foodSales);
    }

    @Override
    public List<FoodSalesResponseDTO> getAllFoodSales() {
        return foodSalesRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<FoodSalesResponseDTO> getFoodSalesByDate(LocalDate dateFoodSales) {
        return FoodSalesMapper.toListFoodSalesResponseDTO(foodSalesRepository.findByDateFoodSales(dateFoodSales));
    }

    @Override
    public List<FoodSalesResponseDTO> getFoodSalesByPeriod(String period) {
        return FoodSalesMapper.toListFoodSalesResponseDTO(foodSalesRepository.findByPeriod(period.toUpperCase()));
    }

    @Override
    public FoodSalesResponseDTO updateFoodSales(Long id, FoodSalesRequestDTO foodSalesRequestDTO) {
        FoodSales foodSales = foodSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food sales not found"));
        foodSales.setTotalFoodSales(foodSalesRequestDTO.getTotalFoodSales());
        foodSales.setLeftoverFoodSales(foodSalesRequestDTO.getLeftoverFoodSales());
        foodSales.setTotalSalesFoodPrice((foodSalesRequestDTO.getTotalFoodSales() - foodSalesRequestDTO.getLeftoverFoodSales()) * foodSalesRequestDTO.getTotalFoodSales());
        foodSales.setDateFoodSales(foodSalesRequestDTO.getDateFoodSales());
        foodSales.setPeriod(foodSalesRequestDTO.getDateFoodSales().getMonth().toString());

        foodSalesRepository.save(foodSales);
        return convertToResponse(foodSales);
    }

    @Override
    public FoodSalesResponseDTO deleteFoodSales(Long id) {
        FoodSales foodSales = foodSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food sales not found"));
        foodSalesRepository.delete(foodSales);
        return convertToResponse(foodSales);
    }

    private FoodSalesResponseDTO convertToResponse(FoodSales foodSales) {
        return FoodSalesResponseDTO.builder()
                .idFoodSales(foodSales.getIdFoodSales())
                .usersResponseDTO(convertToResponse(foodSales.getUser()))
                .foodsResponseDTO(convertToResponse(foodSales.getFoods()))
                .totalFoodSalesPrice(foodSales.getTotalSalesFoodPrice())
                .dateFoodSales(foodSales.getDateFoodSales())
                .period(foodSales.getPeriod())
                .build();
    }

    private UsersResponseDTO convertToResponse(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    private FoodsResponseDTO convertToResponse(Foods foods) {
        return FoodsResponseDTO.builder()
                .idFood(foods.getIdFood())
                .usersResponseDTO(convertToResponse(foods.getUser()))
                .foodName(foods.getFoodName())
                .foodPrice(foods.getFoodPrice())
                .build();
    }

}
