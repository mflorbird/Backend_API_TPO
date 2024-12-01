package com.naiki.ecommerce.controllers.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private boolean isValid; // Indica si el checkout fue exitoso
    private String message; // Mensaje descriptivo del resultado
    private List<InvalidItem> invalidItems; // Lista de productos inválidos
    private Map<String, Validation> validations; // Detalles de validaciones por producto

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvalidItem {
        private String model;
        private String size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Validation {
        private boolean stockAvailable;
        private int requestedQuantity;
        private int maxQuantityAvailable;
    }
}