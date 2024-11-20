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
@Table(name = "profits")
public class Profits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profit")
    private Long idProfit;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @OneToOne
    @JoinColumn(name = "id_sales", nullable = false)
    private Sales sales;

    @OneToOne
    @JoinColumn(name = "id_purchase", nullable = false)
    private Purchases purchases;

    @Column(name = "total_profits", nullable = false)
    private Long totalProfit;

    @Column(name = "date_profit", nullable = false)
    private LocalDate dateProfit;
}
