package com.naiki.ecommerce.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductoDisponible {
    private String productoId;
    private String carritoId;
    private String itemId;
    private int cantidadMaxima;
    private boolean disponible;


}
