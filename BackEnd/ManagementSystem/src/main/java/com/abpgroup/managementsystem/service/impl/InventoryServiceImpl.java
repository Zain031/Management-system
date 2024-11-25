package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    public final InventoryRepository inventoryRepository;
    public final UsersRepository usersRepository;

    @Override
    public List<InventoryResponseDTO> getAllInventory() {
        return List.of();
    }

    @Override
    public InventoryResponseDTO getInventoryById(Long id) {
        return null;
    }

    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        return null;
    }

    @Override
    public InventoryResponseDTO updateInventory(InventoryRequestDTO inventoryRequestDTO) {
        return null;
    }

    @Override
    public void deleteInventoryById(Long id) {

    }
}
