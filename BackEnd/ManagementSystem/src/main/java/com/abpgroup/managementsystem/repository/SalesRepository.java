package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository <Sales, Long> {
}
