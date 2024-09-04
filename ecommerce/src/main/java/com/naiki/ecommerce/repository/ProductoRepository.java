package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long>  {

    List<Producto> findByDestacado(Boolean destacado);
    List<Producto> findByCategoria(String categoria);

    Optional<Producto> findById(Long id);
}
