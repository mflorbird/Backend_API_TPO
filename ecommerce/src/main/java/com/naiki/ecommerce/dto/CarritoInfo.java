package com.naiki.ecommerce.dto;

import java.time.LocalDateTime;

public class CarritoInfo {
    private Long id;
    private LocalDateTime fechaTransaccion;

    public CarritoInfo(Long id, LocalDateTime fechaTransaccion) {
        this.id = id;
        this.fechaTransaccion = fechaTransaccion;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }
}
