package com.naiki.ecommerce.repository.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "productos_favoritos")
public class ProductoFavorito {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "usuario_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "producto_id")
        private Producto producto;

        @Column(name = "fecha_favorito")
        private Date fechaFavorito;
}
