package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "food_sales")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_food_sales")
    private Long idFoodSales;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_food", nullable = false)
    private Foods foods;

    @Column(name = "total_food_sales", nullable = false)
    private Long totalFoodSales;

    @Column(name = "leftover_food_sales", nullable = false)
    private Long leftoverFoodSales;

    @Column(name = "total_sales_food_price", nullable = false)
    private Long totalSalesFoodPrice;

    @Column(name = "date_food_sales", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFoodSales;

    @Column(name = "period", nullable = false)
    private String period;
}
