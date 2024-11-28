package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "talle")
    private String talle;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

}