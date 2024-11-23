package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.DrinkSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DrinkSalesRepository extends JpaRepository<DrinkSales, Long> {
    List<DrinkSales> findByDateDrinkSales(LocalDate dateDrinkSales);

    List<DrinkSales> findByPeriod(String period);
}
