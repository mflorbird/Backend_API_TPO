package com.naiki.ecommerce.repository.entity;

import com.naiki.ecommerce.dto.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("model")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @JsonProperty("description")
    @Column(name = "descripcion")
    private String descripcion;

    @JsonProperty("category")
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @JsonProperty("price")
    @Column(name = "precio", nullable = false)
    private Double precio;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "producto_stock_id")
    private List<SizeStock> stockTotal;

    @JsonProperty("image")
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGTEXT")
    private String imagen;

    @JsonProperty("featured")
    @Column(name = "destacado")
    private Boolean destacado;

    @JsonProperty("created_at")
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @JsonProperty("updated_at")
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }


    // Relaciones comentadas: pueden habilitarse cuando tengas los modelos de usuario definidos.
    /*
    @ManyToOne
    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id", nullable = false)
    private User usuarioCreacion;

    @ManyToOne
    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id")
    private User usuarioModificacion;
    */
}
