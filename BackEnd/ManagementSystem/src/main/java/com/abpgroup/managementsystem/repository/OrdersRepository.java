package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {

    @Query("SELECT o FROM Orders o WHERE o.period=:periodUpper AND o.years=:years AND o.status='COMPLETED' order by o.orderDate desc")
    List<Orders> getOrdersByPeriodAndYears(@Param("periodUpper") String periodUpper, @Param("years") long years);

    @Query("SELECT o FROM Orders o WHERE DATE(o.orderDate) = :datePurchases order by o.orderDate desc")
    List<Orders> getOrdersByDatePurchases(LocalDate datePurchases);

    @Query("SELECT o FROM Orders o WHERE o.years=:years and o.status='COMPLETED' order by o.orderDate desc" )
    List<Orders> getOrdersByYears(Long years);



    @Query("SELECT o FROM Orders o WHERE o.period = :upperCase AND o.years = :years")
    List<Orders> getOrdersByPeriodYears(String upperCase, Long years);


    @Query("SELECT o FROM Orders o WHERE UPPER(o.period) = :periodUpper AND o.years = :years AND o.status = :status order by o.orderDate desc")
    Page<Orders> getOrdersByPeriodYearsAndStatus(
            @Param("periodUpper") String periodUpper,
            @Param("years") Long years,
            @Param("status") Orders.OrderStatus status,
            Pageable pageable
    );

    @Query("SELECT o FROM Orders o WHERE o.period = :upperCase AND o.years = :years order by o.orderDate desc")
    Page<Orders> getOrdersAllByPeriodAndYears(String upperCase, Long years, Pageable pageable);
}
