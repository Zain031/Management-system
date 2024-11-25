package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
