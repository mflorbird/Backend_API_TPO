package com.naiki.ecommerce.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //nuevo campo agregado xNATESTA 19/11
    @Column(name = "model")
    private String model;

    //nuevo campo agregado xNATESTA 19/11
    @Column(name = "featured")
    private Boolean featured;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "image")
    private String image;

//    @Column(name = "nombre")
//    private String nombre;
//
//    @Column(name = "descripcion")
//    private String descripcion;
//
//    @Column(name = "categoria")
//    private String categoria;
//
//    @Column(name = "precio")
//    private Double precio;

    @Column(name = "stock")
    private Integer stock;

//    @Column(name = "imagen")
//    private String imagen;

    @Column(name = "destacado")
    private Boolean destacado;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    //DIEGO, AGREGO ESTO PORQUE CREO QUE LO NECESITO PARA EL CHECKOUT.
    @ElementCollection
    private List<Stock> stockTotal;

    // Clase embebida `Stock` esto lo agrego porque en front tenemos size y stock dentro de producto como varios.
    @Embeddable
    @Data
    public static class Stock {
        private String size;
        private String stock;

        public Stock() {}
        public Stock(String size, String stock) {
            this.size = size;
            this.stock = stock;
        }
    }


    public Producto (){
        this.fechaCreacion = LocalDateTime.now();
    }

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
