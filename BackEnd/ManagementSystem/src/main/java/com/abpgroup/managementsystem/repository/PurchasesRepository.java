package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesRepository extends JpaRepository <Purchases, Long> {
}
