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
@Table(name = "inventory")
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
    private Double materialPriceUnit;

    @Column(name = "material_quantity", nullable = false)
    private Double materialQuantity;

    @Column(name = "material_discount", nullable = false)
    private Double materialDiscount;

    @Column(name="material_price_discount", nullable = false)
    private Double materialPriceDiscount;

    @Column(name = "material_total_price", nullable = false)
    private Double materialTotalPrice;

    @Column(name = "date_material_buy", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMaterialBuy;

    @Column(name = "period", nullable = false)
    private String period;

    @Column(name = "years", nullable = false)
    private Long years;

    // Enum Role
    public enum Category {
        FOODSTUFF, TOOL, ETC
    }
}
