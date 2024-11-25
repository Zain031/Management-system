package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sales")
    private Long idSales;


    @Column(name = "total_sales", nullable = false)
    private Long totalSales;

    @Column(name = "total_leftover_sales", nullable = false)
    private Long totalLeftoverSales;

    @Column(name = "total_sales_price", nullable = false)
    private Long totalSalesPrice;

    @Column(name = "total_leftover_sales_price", nullable = false)
    private Long totalLeftoverSalesPrice;

    @Column(name = "date_sales", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSales;

    @Column(name = "period", nullable = false)
    private String period;
}
