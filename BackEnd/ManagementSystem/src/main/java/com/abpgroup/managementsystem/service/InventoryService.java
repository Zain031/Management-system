package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;

import java.util.List;

public interface InventoryService {
    List<InventoryResponseDTO> getAllInventory();
    InventoryResponseDTO getInventoryById(Long id);
    InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO);
    InventoryResponseDTO updateInventory(InventoryRequestDTO inventoryRequestDTO);
    void deleteInventoryById(Long id);
}
