package com.abpgroup.managementsystem.model.entity;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "id_food", nullable = false)
    private Foods food;

    @ManyToOne
    @JoinColumn(name = "id_drink", nullable = false)
    private Drinks drink;

    @Column(name="total_sales_price", nullable = false)
    private Long totalSalesPrice;

    @Column(name = "date_price", nullable = false)
    private LocalDate datePrice;
}
