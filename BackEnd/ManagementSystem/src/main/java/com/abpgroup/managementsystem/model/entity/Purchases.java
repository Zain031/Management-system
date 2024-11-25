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
@Table(name = "purchases")
public class Purchases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_purchase")
    private Long idPurchase;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @OneToOne
    @JoinColumn(name = "id_foodstuff", nullable = false)
    private Inventory foodstuff;

    @OneToOne
    @JoinColumn(name = "id_tool", nullable = false)
    private Tools tool;

    @Column(name = "purchase_total_price", nullable = false)
    private Long purchaseTotalPrice;

    @Column(name = "date_purchase", nullable = false)
    private LocalDate datePurchase;
}
