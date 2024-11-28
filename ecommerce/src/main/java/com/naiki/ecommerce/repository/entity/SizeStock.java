package com.naiki.ecommerce.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SizeStock {

    @Id
    private Long id;
    @JsonProperty("stock")
    private int cantidad;
    @JsonProperty("size")
    private String talle;

}
