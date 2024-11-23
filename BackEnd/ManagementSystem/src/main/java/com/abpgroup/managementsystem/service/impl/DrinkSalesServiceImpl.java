package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.mapper.DrinkSalesMapper;
import com.abpgroup.managementsystem.model.dto.request.DrinkSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinkSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinksResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.DrinkSales;
import com.abpgroup.managementsystem.model.entity.Drinks;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.DrinkSalesRepository;
import com.abpgroup.managementsystem.repository.DrinksRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.DrinkSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkSalesServiceImpl implements DrinkSalesService {
    private final DrinkSalesRepository drinkSalesRepository;
    private final UsersRepository usersRepository;
    private final DrinksRepository drinksRepository;

    @Override
    public DrinkSalesResponseDTO createDrinkSales(DrinkSalesRequestDTO drinkSalesRequestDTO) {
        Drinks drinks = drinksRepository.findById(drinkSalesRequestDTO.getIdDrink())
                .orElseThrow(() -> new IllegalArgumentException("Drink not found"));
        Users users = usersRepository.findById(drinkSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        LocalDate dateDrinkSales = drinkSalesRequestDTO.getDateDrinkSales();

        DrinkSales drinkSales = DrinkSales.builder()
                .user(users)
                .drinks(drinks)
                .totalDrinkSales(drinkSalesRequestDTO.getTotalDrinkSales())
                .leftoverDrinkSales(drinkSalesRequestDTO.getTotalDrinkSales())
                .totalSalesDrinkPrice((drinks.getDrinkPrice() * drinkSalesRequestDTO.getTotalDrinkSales()) - (drinks.getDrinkPrice() * drinkSalesRequestDTO.getLeftoverDrinkSales()))
                .dateDrinkSales(drinkSalesRequestDTO.getDateDrinkSales())
                .period(dateDrinkSales.getMonth().toString())
                .build();
        drinkSalesRepository.save(drinkSales);
        return convertToResponse(drinkSales);
    }

    @Override
    public List<DrinkSalesResponseDTO> getAllDrinkSales() {
        return drinkSalesRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<DrinkSalesResponseDTO> getDrinkSalesByDate(LocalDate dateDrinkSales) {
        return DrinkSalesMapper.toListDrinkSalesResponseDTO(drinkSalesRepository.findByDateDrinkSales(dateDrinkSales));
    }

    @Override
    public List<DrinkSalesResponseDTO> getDrinkSalesByPeriod(String period) {
        return DrinkSalesMapper.toListDrinkSalesResponseDTO(drinkSalesRepository.findByPeriod(period.toUpperCase()));
    }

    @Override
    public DrinkSalesResponseDTO updateDrinkSales(Long id, DrinkSalesRequestDTO drinkSalesRequestDTO) {
        DrinkSales drinkSales = drinkSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drink sales not found"));
        drinkSales.setTotalDrinkSales(drinkSalesRequestDTO.getTotalDrinkSales());
        drinkSales.setLeftoverDrinkSales(drinkSalesRequestDTO.getLeftoverDrinkSales());
        drinkSales.setTotalSalesDrinkPrice((drinkSalesRequestDTO.getTotalDrinkSales() - drinkSalesRequestDTO.getLeftoverDrinkSales()) * drinkSalesRequestDTO.getTotalDrinkSales());
        drinkSales.setDateDrinkSales(drinkSalesRequestDTO.getDateDrinkSales());
        drinkSales.setPeriod(drinkSalesRequestDTO.getDateDrinkSales().getMonth().toString());

        drinkSalesRepository.save(drinkSales);
        return convertToResponse(drinkSales);
    }

    @Override
    public DrinkSalesResponseDTO deleteDrinkSales(Long id) {
        DrinkSales drinkSales = drinkSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drink sales not found"));
        drinkSalesRepository.delete(drinkSales);
        return convertToResponse(drinkSales);
    }

    private DrinkSalesResponseDTO convertToResponse(DrinkSales drinkSales) {
        return DrinkSalesResponseDTO.builder()
                .idDrinkSales(drinkSales.getIdDrinkSales())
                .usersResponseDTO(convertToResponse(drinkSales.getUser()))
                .drinksResponseDTO(convertToResponse(drinkSales.getDrinks()))
                .totalDrinkSalesPrice(drinkSales.getTotalSalesDrinkPrice())
                .dateDrinkSales(drinkSales.getDateDrinkSales())
                .period(drinkSales.getPeriod())
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

    private DrinksResponseDTO convertToResponse(Drinks drinks) {
        return DrinksResponseDTO.builder()
                .idDrink(drinks.getIdDrink())
                .usersResponseDTO(convertToResponse(drinks.getUser()))
                .drinkName(drinks.getDrinkName())
                .drinkPrice(drinks.getDrinkPrice())
                .build();
    }

}
