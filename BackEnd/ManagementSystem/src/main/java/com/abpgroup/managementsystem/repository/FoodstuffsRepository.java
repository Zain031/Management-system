package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Foodstuffs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodstuffsRepository extends JpaRepository<Foodstuffs, Long> {
}
