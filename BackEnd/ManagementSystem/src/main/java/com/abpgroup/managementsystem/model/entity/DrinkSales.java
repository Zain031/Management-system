package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "drink_sales")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrinkSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_drink_sales")
    private Long idDrinkSales;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_drink", nullable = false)
    private Drinks drinks;

    @Column(name = "total_drink_sales", nullable = false)
    private Long totalDrinkSales;

    @Column(name = "leftover_drink_sales", nullable = false)
    private Long leftoverDrinkSales;

    @Column(name = "total_sales_drink_price", nullable = false)
    private Long totalSalesDrinkPrice;

    @Column(name = "date_drink_sales", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDrinkSales;

    @Column(name = "period", nullable = false)
    private String period;
}
