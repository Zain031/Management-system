package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Foodstuffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodstuffsRepository extends JpaRepository<Foodstuffs, Long> {
}
