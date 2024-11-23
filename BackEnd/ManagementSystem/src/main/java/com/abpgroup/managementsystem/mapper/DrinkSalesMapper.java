package com.abpgroup.managementsystem.mapper;

import com.abpgroup.managementsystem.model.dto.response.DrinkSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinksResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.DrinkSales;

import java.util.ArrayList;
import java.util.List;

public class DrinkSalesMapper {
    public static List<DrinkSalesResponseDTO> toListDrinkSalesResponseDTO(List<DrinkSales> drinkSales) {
        List<DrinkSalesResponseDTO> result = new ArrayList<>();

        for (DrinkSales data : drinkSales) {
            UsersResponseDTO user = UsersResponseDTO.builder()
                    .idUser(data.getUser().getIdUser())
                    .email(data.getUser().getEmail())
                    .name(data.getUser().getName())
                    .role(data.getUser().getRole().name())
                    .build();

            UsersResponseDTO drinkCreator = UsersResponseDTO.builder()
                    .idUser(data.getDrinks().getUser().getIdUser())
                    .email(data.getDrinks().getUser().getEmail())
                    .name(data.getDrinks().getUser().getName())
                    .role(data.getDrinks().getUser().getRole().name())
                    .build();

            DrinksResponseDTO drink = DrinksResponseDTO.builder()
                    .idDrink(data.getDrinks().getIdDrink())
                    .drinkName(data.getDrinks().getDrinkName())
                    .drinkPrice(data.getDrinks().getDrinkPrice())
                    .usersResponseDTO(drinkCreator)
                    .build();

            result.add(DrinkSalesResponseDTO.builder()
                    .idDrinkSales(data.getIdDrinkSales())
                    .totalDrinkSalesPrice(data.getTotalDrinkSales())
                    .dateDrinkSales(data.getDateDrinkSales())
                    .period(data.getPeriod())
                    .usersResponseDTO(user)
                    .drinksResponseDTO(drink)
                    .build());
        }

        return result;
    }

}
