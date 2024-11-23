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
        if (drinksRequestDTO.getDrinkName() == null || drinksRequestDTO.getDrinkName().isEmpty()) {
            throw new IllegalArgumentException("Drink name cannot be empty");
        }

        if (drinksRequestDTO.getDrinkPrice() <= 0) {
            throw new IllegalArgumentException("Drink price must be greater than zero");
        }

        Users user = usersRepository.findById(drinksRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Drinks drinks = Drinks.builder()
                .user(user)
                .drinkName(drinksRequestDTO.getDrinkName())
                .drinkPrice(drinksRequestDTO.getDrinkPrice())
                .build();

        drinksRepository.save(drinks);

        return convertToResponse(drinks);
    }


    @Override
    public List<DrinksResponseDTO> getAllDrinks() {
       return drinksRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public DrinksResponseDTO getDrinksById(Long id) {
        return drinksRepository.findById(id).map(this::convertToResponse).orElse(null);
    }

    @Override
    public DrinksResponseDTO updateDrinks(Long id, DrinksRequestDTO drinksRequestDTO) {
        Drinks drinks = drinksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drinks not found"));
        Users user = usersRepository.findById(drinksRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        drinks.setUser(user);
        drinks.setDrinkName(drinksRequestDTO.getDrinkName());
        drinks.setDrinkPrice(drinksRequestDTO.getDrinkPrice());
        drinksRepository.save(drinks);
        return convertToResponse(drinks);
    }

    @Override
    public DrinksResponseDTO deleteDrinks(Long id) {
        Drinks drinks = drinksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drinks not found"));
        drinksRepository.delete(drinks);
        return convertToResponse(drinks);
    }

    private DrinksResponseDTO convertToResponse(Drinks drinks) {
        return DrinksResponseDTO.builder()
                .idDrink(drinks.getIdDrink())
                .usersResponseDTO(convertToUsersResponseDTO(drinks.getUser()))
                .drinkName(drinks.getDrinkName())
                .drinkPrice(drinks.getDrinkPrice())
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
