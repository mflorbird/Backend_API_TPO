package com.naiki.ecommerce.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "destacado")
    private Boolean destacado;

    @Column(name = "fechaCreacion")
    private String fechaCreacion;

    @Column(name = "fechaModificacion")
    private String fechaModificacion;

//    @ManyToOne
//    @JoinColumn(name = "usuarioCreacion", referencedColumnName = "id", nullable = false)
//    @Column(name = "usuarioCreacion")
//    private User usuarioCreacion;
//
//    @ManyToOne
//    @JoinColumn(name = "usuarioModificacion", referencedColumnName = "id", nullable = true)
//    @Column(name = "usuarioModificacion")
//    private User usuarioModificacion;

    @Column(name = "estado")
    private String estado;
    
}
