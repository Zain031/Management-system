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
@Table(name = "materials")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Long idMaterial;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name="material_price_unit", nullable = false)
    private Long materialPriceUnit;

    @Column(name = "material_quantity", nullable = false)
    private Long materialQuantity;

    @Column(name = "material_discount", nullable = false)
    private Long materialDiscount;

    @Column(name="material_price_discount", nullable = false)
    private Long materialPriceDiscount;

    @Column(name = "material_total_price", nullable = false)
    private Long materialTotalPrice;

    @Column(name = "date_material_buy", nullable = false)
    private LocalDate dateMaterialBuy;

    // Enum Role
    public enum Category {
        FOODSTUFF, TOOL
    }
}
