package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Page<Inventory> findAllByCategory(Inventory.Category category, Pageable pageable);

    @Query("SELECT SUM(i.materialQuantity) " +
            "FROM Inventory i " +
            "WHERE i.dateMaterialBuy = :datePurchases")
    Double calculateTotalPurchasesByDate(LocalDate datePurchases);

    @Query("SELECT SUM(i.materialTotalPrice) " +
            "FROM Inventory i " +
            "WHERE i.dateMaterialBuy = :datePurchases")
    Double calculateTotalPurchasesPriceByDate(LocalDate datePurchases);

    @Query("SELECT SUM(i.materialQuantity) " +
            "FROM Inventory i " +
            "WHERE i.period = :periodUpper")
    Double calculateTotalPurchasesByPeriod(String periodUpper);

    @Query("SELECT SUM(i.materialTotalPrice) " +
            "FROM Inventory i " +
            "WHERE i.period = :periodUpper")
    Double calculateTotalPurchasesPriceByPeriod(String periodUpper);

    @Query("SELECT i FROM Inventory i " +
            "WHERE i.dateMaterialBuy = :datePurchases")
    List<Inventory> findInventoryByDatePurchases(LocalDate datePurchases);

    @Query("SELECT i FROM Inventory i " +
            "WHERE i.period = :periodUpper AND i.years = :years")
    List<Inventory> findInventoryByPeriodAndYears(String periodUpper, Long years);

    @Query("SELECT i FROM Inventory i " +
            "WHERE lower(i.materialName) LIKE lower(CONCAT('%', :materialName, '%')) ")
    Page<Inventory> getInventoryByMaterialName(String materialName, Pageable sortedByDateMaterialBuy);
}
