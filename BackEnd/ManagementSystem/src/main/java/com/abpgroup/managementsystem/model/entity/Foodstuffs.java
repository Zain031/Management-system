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
@Table(name = "foodstuffs")
public class Foodstuffs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foodstuff")
    private Long idFoodstuff;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name = "foodstuff_name", nullable = false)
    private String foodstuffName;

    @Column(name="foodstuff_price_unit", nullable = false)
    private Long foodstuffPriceUnit;

    @Column(name = "foodstuff_quantity", nullable = false)
    private Long foodstuffQuantity;

    @Column(name = "foodstuff_discount", nullable = false)
    private Long foodstuffDiscount;

    @Column(name="foodstuff_price_discount", nullable = false)
    private Long foodstuffPriceDiscount;

    @Column(name = "foodstuff_total_price", nullable = false)
    private Long foodstuffTotalPrice;

    @Column(name = "date_foodstuff_buy", nullable = false)
    private LocalDate dateFoodstuffBuy;
}
