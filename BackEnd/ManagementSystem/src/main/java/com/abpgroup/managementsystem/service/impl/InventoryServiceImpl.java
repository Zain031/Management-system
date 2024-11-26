package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import com.abpgroup.managementsystem.model.entity.Inventory;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    public final InventoryRepository inventoryRepository;
    public final UsersRepository usersRepository;

    @Override
    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream().sorted(Comparator.comparingLong(Inventory::getMaterialTotalPrice)).map(this::toInventoryResponseDTO).toList();
    }

    @Override
    public Page<InventoryResponseDTO> getAllInventoryByCategory(Pageable pageable, String category) {
        Page<Inventory> inventories = inventoryRepository.findAllByCategory(Inventory.Category.valueOf(category.toUpperCase()), pageable);
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

        Inventory inventory = Inventory.builder()
                .user(user)
                .category(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()))
                .materialName(inventoryRequestDTO.getMaterialName())
                .materialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit())
                .materialQuantity(inventoryRequestDTO.getMaterialQuantity())
                .materialDiscount(inventoryRequestDTO.getMaterialDiscount())
                .materialPriceDiscount(inventoryRequestDTO.getMaterialPriceUnit()-(inventoryRequestDTO.getMaterialPriceUnit() * inventoryRequestDTO.getMaterialDiscount()))
                .materialTotalPrice(inventoryRequestDTO.getMaterialPriceUnit()-(inventoryRequestDTO.getMaterialPriceUnit() * inventoryRequestDTO.getMaterialDiscount()) * inventoryRequestDTO.getMaterialQuantity())
                .build();

        inventoryRepository.save(inventory);
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = findInventoryByIdOrThrow(id);

        Users user = usersRepository.findById(inventoryRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        inventory.setUser(user);
        inventory.setCategory(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()));
        inventory.setMaterialName(inventoryRequestDTO.getMaterialName());
        inventory.setMaterialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit());
        inventory.setMaterialQuantity(inventoryRequestDTO.getMaterialQuantity());
        inventory.setMaterialDiscount(inventoryRequestDTO.getMaterialDiscount());
        inventory.setMaterialPriceDiscount(inventoryRequestDTO.getMaterialPriceUnit()-(inventoryRequestDTO.getMaterialPriceUnit() * inventoryRequestDTO.getMaterialDiscount()));
        inventory.setMaterialTotalPrice(inventoryRequestDTO.getMaterialPriceUnit()-(inventoryRequestDTO.getMaterialPriceUnit() * inventoryRequestDTO.getMaterialDiscount()) * inventoryRequestDTO.getMaterialQuantity());
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public void deleteInventoryById(Long id) {
        Inventory inventory = findInventoryByIdOrThrow(id);
        inventoryRepository.delete(inventory);
    }

    private Inventory findInventoryByIdOrThrow(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
    }

    private InventoryResponseDTO toInventoryResponseDTO(Inventory inventory) {
        return InventoryResponseDTO.builder()
                .idMaterial(inventory.getIdMaterial())
                .idUser(inventory.getUser().getIdUser())
                .materialCategory(inventory.getCategory().toString())
                .materialName(inventory.getMaterialName())
                .materialPriceUnit(inventory.getMaterialPriceUnit())
                .materialQuantity(inventory.getMaterialQuantity())
                .materialDiscount(inventory.getMaterialDiscount())
                .materialPriceDiscount(inventory.getMaterialPriceDiscount())
                .materialTotalPrice(inventory.getMaterialTotalPrice())
                .dateMaterialBuy(inventory.getDateMaterialBuy())
                .build();
    }
}
