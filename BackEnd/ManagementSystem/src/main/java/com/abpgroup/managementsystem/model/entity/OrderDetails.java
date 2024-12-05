package com.abpgroup.managementsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Products products;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;

    @Column(name = "subtotal_price", nullable = false)
    private Double subtotalPrice;
}
