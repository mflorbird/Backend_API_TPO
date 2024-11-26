package com.naiki.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @Data
    public class SizeStock {
        @JsonProperty("stock")
        private int cantidad;
        @JsonProperty("size")
        private String talle;

    }

    @JsonProperty("model")
    private String nombre;
    @JsonProperty("category")
    private String categoria;
    @JsonProperty("description")
    private String descripcion;
    @JsonProperty("price")
    private Double precio;
    @JsonProperty("stock")
    private List<SizeStock> stockTotal;
    @JsonProperty("featured")
    private boolean destacado;
    @JsonProperty("image")
    private String imagen;
}
