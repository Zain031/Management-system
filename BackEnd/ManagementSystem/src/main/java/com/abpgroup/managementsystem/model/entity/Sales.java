package com.abpgroup.managementsystem.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sales")
    private Long idSales;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_food_sales", nullable = false)
    private FoodSales foodSales;

    @ManyToOne
    @JoinColumn(name = "id_drink_sales", nullable = false)
    private DrinkSales drinkSales;

    @Column(name="total_sales_price", nullable = false)
    private Long totalSalesPrice;

    @Column(name = "date_price", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSalesPrice;

    @Column(name="period", nullable = false)
    private String period;
}
