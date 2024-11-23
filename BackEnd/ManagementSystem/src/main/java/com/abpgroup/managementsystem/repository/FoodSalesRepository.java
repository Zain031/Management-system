package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.FoodSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface FoodSalesRepository extends JpaRepository <FoodSales, Long> {

    List<FoodSales> findByDateFoodSales(LocalDate dateFoodSales);

    List<FoodSales> findByPeriod(String period);
}
