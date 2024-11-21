package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Profits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfitsRepository extends JpaRepository <Profits, Long> {
}
