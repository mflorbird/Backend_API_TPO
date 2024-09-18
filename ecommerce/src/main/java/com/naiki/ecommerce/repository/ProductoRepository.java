package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>  {

    List<Producto> findByDestacado(Boolean destacado);
    List<Producto> findByCategoria(String categoria);

    Optional<Producto> findById(Long id);

    @Query(value = "UPDATE productos" +
            "SET stock = ?2" +
            "WHERE id = ?1", nativeQuery = true)
    List<Producto> modificarStock(long id, int cantidad);
}
