package com.naiki.ecommerce.repository.entity;

import lombok.Data;


@Data
public class ProductoRequest {
    private String productoId;
    private String carritoId;
    private int cantidad;

}
