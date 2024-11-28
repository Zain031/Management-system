package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.ProductSales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {

    Page<ProductSales> findProductSalesByDateProductSales(LocalDate dateProductSales, Pageable pageable);

    List<ProductSales> findProductSalesByDateProductSales(LocalDate dateProductSales);

    Page<ProductSales> findProductSalesByProductCategories(String productCategories, Pageable pageable);

    @Query(
            "SELECT SUM(ps.totalProductSales) " +
                    "FROM ProductSales ps " +
                    "WHERE ps.period = :period"
    )
    Long calculateTotalSalesByPeriod(String period);

    @Query(
            "SELECT SUM(ps.totalProductSalesPrice) " +
                    "FROM ProductSales ps " +
                    "WHERE ps.period = :period"
    )
    Long calculateTotalSalesPriceByPeriod(String period);

    @Query(
            "SELECT SUM(ps.totalLeftoverProductSalesPrice) " +
                    "FROM ProductSales ps " +
                    "WHERE ps.period = :period"
    )
    Long calculateTotalLeftoverSalesPriceByPeriod(String period);

    @Query(
            "SELECT SUM(ps.leftoverProductSales) " +
                    "FROM ProductSales ps " +
                    "WHERE ps.period = :period"
    )
    Long calculateTotalLeftoverSalesByPeriod(String period);

    @Query("SELECT ps FROM ProductSales ps " +
            "WHERE ps.period = :upperCase AND ps.years = :years")
    Page<ProductSales> findProductSalesByPeriodAndYears(String upperCase, Long years, Pageable sortedByDateProductSales);

    @Query("SELECT ps FROM ProductSales ps " +
            "WHERE ps.period = :periodUpper AND ps.years = :years")
    List<ProductSales> getProductSalesByPeriodAndYears(String periodUpper, Long years);

    @Query("SELECT ps FROM ProductSales ps " +
            "WHERE lower(ps.product.productName) LIKE lower(CONCAT('%', :productName, '%')) ")
    Page<ProductSales> findProductSalesByProductName(String productName, Pageable sortedByDateProductSales);
}
