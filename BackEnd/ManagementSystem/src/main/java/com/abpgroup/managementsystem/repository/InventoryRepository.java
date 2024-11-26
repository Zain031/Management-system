package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Inventory;
import io.micrometer.common.KeyValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByCategory(Inventory.Category category);

    Page<Inventory> findAllByCategory(Inventory.Category category, Pageable pageable);

    KeyValues findAllByMaterialTotalPrice(Long materialTotalPrice, Sort sort);
}
