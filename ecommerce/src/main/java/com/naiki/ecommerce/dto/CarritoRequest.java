package com.naiki.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naiki.ecommerce.repository.entity.ItemCarrito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarritoRequest {

    private Map<Long, ItemCarrito> items;
    private Double precioTotal;
    private Double precioDiscount;
    private Double discount;
    private String estado;
    private LocalDateTime closedAt;

}
