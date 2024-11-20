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
@Table(name = "tools")
public class Tools {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tool")
    private Long idTool;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name = "tool_name", nullable = false)
    private String toolName;

    @Column(name = "tool_price_unit", nullable = false)
    private Integer toolPriceUnit;

    @Column(name = "tool_quantity", nullable = false)
    private Integer toolQuantity;

    @Column(name = "tool_discount", nullable = false)
    private Integer toolDiscount;

    @Column(name = "tool_price_discount", nullable = false)
    private Integer toolPriceDiscount;

    @Column(name = "tool_total_price", nullable = false)
    private Integer toolTotalPrice;

    @Column(name = "date_tool_buy", nullable = false)
    private LocalDate dateToolBuy;
}
