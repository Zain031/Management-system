package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    @Query("SELECT SUM(ps.totalProductSalesPrice) " +
            "FROM ProductSales ps " +
            "WHERE ps.period = :period")
    Long calculateTotalSalesPriceByPeriod(@Param("period") String period);

    @Query("SELECT SUM(ps.totalLeftoverProductSalesPrice) " +
            "FROM ProductSales ps " +
            "WHERE ps.period = :period")
    Long calculateTotalLeftoverSalesPriceByPeriod(@Param("period") String period);

    @Query("SELECT SUM(ps.totalProductSales) " +
            "FROM ProductSales ps " +
            "WHERE ps.period = :period")
    Long calculateTotalSalesByPeriod(@Param("period") String period);

    @Query("SELECT SUM(ps.leftoverProductSales) " +
            "FROM ProductSales ps " +
            "WHERE ps.period = :period")
    Long calculateTotalLeftoverSalesByPeriod(@Param("period") String period);
}
