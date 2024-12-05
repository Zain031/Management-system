package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.period=:periodUpper AND o.years=:years AND o.status='COMPLETED'")
    List<Orders> getOrdersByPeriodAndYears(@Param("periodUpper") String periodUpper, @Param("years") long years);

    @Query("SELECT o FROM Orders o WHERE DATE(o.orderDate) = :datePurchases")
    List<Orders> getOrdersByDatePurchases(LocalDate datePurchases);

}
