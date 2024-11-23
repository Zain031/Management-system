package com.abpgroup.managementsystem.mapper;

import com.abpgroup.managementsystem.model.dto.response.FoodSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.FoodsResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.FoodSales;

import java.util.ArrayList;
import java.util.List;

public class FoodSalesMapper {
    public static List<FoodSalesResponseDTO> toListFoodSalesResponseDTO(List<FoodSales> foodSales) {
        List<FoodSalesResponseDTO> result = new ArrayList<>();

        for (FoodSales data : foodSales) {
            UsersResponseDTO user = UsersResponseDTO.builder()
                    .idUser(data.getUser().getIdUser())
                    .email(data.getUser().getEmail())
                    .name(data.getUser().getName())
                    .role(data.getUser().getRole().name())
                    .build();

            UsersResponseDTO foodCreator = UsersResponseDTO.builder()
                    .idUser(data.getFoods().getUser().getIdUser())
                    .email(data.getFoods().getUser().getEmail())
                    .name(data.getFoods().getUser().getName())
                    .role(data.getFoods().getUser().getRole().name())
                    .build();

            FoodsResponseDTO food = FoodsResponseDTO.builder()
                    .idFood(data.getFoods().getIdFood())
                    .foodName(data.getFoods().getFoodName())
                    .foodPrice(data.getFoods().getFoodPrice())
                    .usersResponseDTO(foodCreator)
                    .build();

            result.add(FoodSalesResponseDTO.builder()
                            .idFoodSales(data.getIdFoodSales())
                            .totalFoodSalesPrice(data.getTotalFoodSales())
                            .dateFoodSales(data.getDateFoodSales())
                            .period(data.getPeriod())
                            .usersResponseDTO(user)
                            .foodsResponseDTO(food)
                    .build());
        }

        return  result;
    }
}
