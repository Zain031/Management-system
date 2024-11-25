package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "product_sales")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product_sales")
    private Long idProductSales;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Products product;

    @Column(name = "total_product_sales", nullable = false)
    private Long totalProductSales;

    @Column(name = "leftover_product_sales", nullable = false)
    private Long leftoverProductSales;

    @Column(name = "date_product_sales", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateProductSales;

    @Column(name = "period", nullable = false)
    private String period;
}
