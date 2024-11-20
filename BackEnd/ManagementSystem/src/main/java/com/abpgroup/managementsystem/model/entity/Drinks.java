package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "drinks")
public class Drinks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_drink")
    private Long idDrink;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name ="drink_name", nullable = false)
    private String drinkName;

    @Column(name = "drink_price", nullable = false)
    private Long drinkPrice;

    @Column(name = "total_drink" ,nullable = false)
    private Long totalDrink;

    @Column(name = "total_drink_price", nullable = false)
    private Long totalDrinkPrice;
}
