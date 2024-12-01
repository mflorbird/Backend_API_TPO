package com.naiki.ecommerce.repository.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("model")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @JsonProperty("price")
    @Column(name = "precio", nullable = false)
    private Double precio;

    @JsonProperty("quantity")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @JsonProperty("size")
    @Column(name = "talle")
    private String talle;

    @JsonProperty("subtotal")
    @Column(name = "subtotal")
    private Double subtotal;

    @JsonProperty("img")
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGTEXT")
    private String imagen;

    @JsonProperty("cart")
    private Long carrito;

}