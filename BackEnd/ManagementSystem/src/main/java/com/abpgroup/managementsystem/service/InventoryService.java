package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface InventoryService {
    Page<InventoryResponseDTO> getAllInventory(Pageable pageable);
    Page<InventoryResponseDTO> getAllInventoryByCategory(Pageable pageable, String category);
    InventoryResponseDTO getInventoryById(Long id);
    InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO);
    InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO);
    void deleteInventoryById(Long id);
    Page<InventoryResponseDTO> getInventoryByMaterialName(String materialName, Pageable pageable);
}
