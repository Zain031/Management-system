package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasesRepository extends JpaRepository <Purchases, Long> {
}
