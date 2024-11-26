package com.naiki.ecommerce.repository.entity;

import com.naiki.ecommerce.dto.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "producto_stock_id")
    private List<SizeStock> stockTotal;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "destacado")
    private Boolean destacado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

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

    public void setStockTotal(List<ProductRequest.SizeStock> stockTotal) {
        this.stockTotal = stockTotal.stream()
                .map(sizeStock -> new SizeStock(null, sizeStock.getCantidad(), sizeStock.getTalle()))
                .toList();
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
