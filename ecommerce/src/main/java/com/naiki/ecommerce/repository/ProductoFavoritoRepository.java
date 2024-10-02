package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.repository.entity.ProductoFavorito;
import com.naiki.ecommerce.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoFavoritoRepository extends JpaRepository<ProductoFavorito, Long> {

    List<ProductoFavorito> findByUserOrderByFechaFavoritoDesc(User user);
    ProductoFavorito findByUserAndProducto(User user, Producto producto);
    @Transactional
    void deleteByUserAndProducto(User user, Producto producto);
}