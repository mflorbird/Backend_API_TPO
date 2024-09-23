package com.naiki.ecommerce.repository.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "productos_visitados")
public class ProductoVisitado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "fecha_visita")
    private Date fechaVisita;

}
