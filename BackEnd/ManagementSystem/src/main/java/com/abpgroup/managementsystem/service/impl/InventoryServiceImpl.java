package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Inventory;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    public final InventoryRepository inventoryRepository;
    public final UsersRepository usersRepository;

    @Override
    public Page<InventoryResponseDTO> getAllInventory(Pageable pageable) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.findAll(sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    @Override
    public Page<InventoryResponseDTO> getAllInventoryByCategory(Pageable pageable, String category) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.findAllByCategory(Inventory.Category.valueOf(category.toUpperCase()), sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    @Override
    public InventoryResponseDTO getInventoryById(Long id) {
        return toInventoryResponseDTO(findInventoryByIdOrThrow(id));
    }

    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        if(inventoryRequestDTO.getMaterialName() == null || inventoryRequestDTO.getMaterialName().isEmpty()){
            throw new IllegalArgumentException("Material name cannot be empty");
        }

        if (inventoryRequestDTO.getMaterialPriceUnit() <= 0) {
            throw new IllegalArgumentException("Material price must be greater than zero");
        }

        if (inventoryRequestDTO.getMaterialCategory() == null || inventoryRequestDTO.getMaterialCategory().isEmpty()) {
            throw new IllegalArgumentException("Material category cannot be empty");
        }

        Users user = usersRepository.findById(inventoryRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Double materialPriceDiscount = inventoryRequestDTO.getMaterialPriceUnit() - (inventoryRequestDTO.getMaterialPriceUnit() * (inventoryRequestDTO.getMaterialDiscount()/100));
        Double materialTotalPrice = materialPriceDiscount * inventoryRequestDTO.getMaterialQuantity();
        Inventory inventory = Inventory.builder()
                .user(user)
                .category(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()))
                .materialName(inventoryRequestDTO.getMaterialName())
                .materialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit())
                .materialQuantity(inventoryRequestDTO.getMaterialQuantity())
                .materialDiscount(inventoryRequestDTO.getMaterialDiscount())
                .materialPriceDiscount(materialPriceDiscount)
                .materialTotalPrice(materialTotalPrice)
                .dateMaterialBuy(inventoryRequestDTO.getDateMaterialBuy())
                .period(inventoryRequestDTO.getDateMaterialBuy().getMonth().name())
                .years(Long.valueOf(inventoryRequestDTO.getDateMaterialBuy().getYear()))
                .build();

        inventoryRepository.save(inventory);
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = findInventoryByIdOrThrow(id);

        Users user = usersRepository.findById(inventoryRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Double materialPriceDiscount = inventoryRequestDTO.getMaterialPriceUnit() - (inventoryRequestDTO.getMaterialPriceUnit() * (inventoryRequestDTO.getMaterialDiscount()/100));
        Double materialTotalPrice = materialPriceDiscount * inventoryRequestDTO.getMaterialQuantity();
        inventory.setUser(user);
        inventory.setCategory(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()));
        inventory.setMaterialName(inventoryRequestDTO.getMaterialName());
        inventory.setMaterialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit());
        inventory.setMaterialQuantity(inventoryRequestDTO.getMaterialQuantity());
        inventory.setMaterialDiscount(inventoryRequestDTO.getMaterialDiscount());
        inventory.setMaterialPriceDiscount(materialPriceDiscount);
        inventory.setMaterialTotalPrice(materialTotalPrice);
        inventory.setDateMaterialBuy(inventoryRequestDTO.getDateMaterialBuy());
        inventory.setPeriod(inventoryRequestDTO.getDateMaterialBuy().getMonth().name());
        inventory.setYears(Long.valueOf(inventoryRequestDTO.getDateMaterialBuy().getYear()));
        inventoryRepository.save(inventory);
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public void deleteInventoryById(Long id) {
        Inventory inventory = findInventoryByIdOrThrow(id);
        inventoryRepository.delete(inventory);
    }

    @Override
    public Page<InventoryResponseDTO> getInventoryByMaterialName(String materialName, Pageable pageable) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.getInventoryByMaterialName(materialName, sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    private Inventory findInventoryByIdOrThrow(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
    }

    private InventoryResponseDTO toInventoryResponseDTO(Inventory inventory) {
        return InventoryResponseDTO.builder()
                .idMaterial(inventory.getIdMaterial())
                .usersResponseDTO(convertToResponse(inventory.getUser()))
                .materialCategory(inventory.getCategory().toString())
                .materialName(inventory.getMaterialName())
                .materialPriceUnit(inventory.getMaterialPriceUnit())
                .materialQuantity(inventory.getMaterialQuantity())
                .materialDiscount(inventory.getMaterialDiscount())
                .materialPriceDiscount(inventory.getMaterialPriceDiscount())
                .materialTotalPrice(inventory.getMaterialTotalPrice())
                .dateMaterialBuy(inventory.getDateMaterialBuy())
                .period(inventory.getPeriod())
                .years(inventory.getYears())
                .build();
    }

    private UsersResponseDTO convertToResponse(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
