package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.DrinksRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.DrinksResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Drinks;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.DrinksRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.DrinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinksServiceImpl implements DrinksService {
    private final DrinksRepository drinksRepository;
    private final UsersRepository usersRepository;

    @Override
    public DrinksResponseDTO createDrinks(DrinksRequestDTO drinksRequestDTO) {
        Users user = usersRepository.findById(drinksRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Drinks drinks= Drinks.builder()
                .user(user)
                .drinkName(drinksRequestDTO.getDrinkName())
                .drinkPrice(drinksRequestDTO.getDrinkPrice())
                .totalDrink(drinksRequestDTO.getTotalDrink())
                .totalDrinkPrice(drinksRequestDTO.getDrinkPrice()*drinksRequestDTO.getTotalDrink())
                .build();
        drinksRepository.save(drinks);
        return convertToResponse(drinks);
    }

    @Override
    public List<DrinksResponseDTO> getAllDrinks() {
        return List.of();
    }

    @Override
    public DrinksResponseDTO getDrinksById(Long id) {
        return null;
    }

    @Override
    public DrinksResponseDTO updateDrinks(Long id, DrinksRequestDTO drinksRequestDTO) {
        return null;
    }

    @Override
    public DrinksResponseDTO deleteDrinks(Long id) {
        return null;
    }

    private DrinksResponseDTO convertToResponse(Drinks drinks) {
        return DrinksResponseDTO.builder()
                .idDrink(drinks.getIdDrink())
                .usersResponseDTO(convertToUsersResponseDTO(drinks.getUser()))
                .drinkName(drinks.getDrinkName())
                .drinkPrice(drinks.getDrinkPrice())
                .totalDrink(drinks.getTotalDrink())
                .totalDrinkPrice(drinks.getTotalDrinkPrice())
                .build();
    }

    private UsersResponseDTO convertToUsersResponseDTO(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
