package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity // tabla en la BD.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<Long, ItemCarrito> items;

    @Column(name = "estado")
    private String estado = "activo";

    @Column(name = "precio_total")
    private Double precioTotal;

    @Column(name = "precio_discount")
    private Double precioDiscount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    public Carrito(Long userId) {
        this.userId = userId;
    }

}