package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>  {

    List<Producto> findByDestacado(Boolean destacado);
    List<Producto> findByCategoria(String categoria);

    @Transactional
    @Modifying
    @Query(value = "UPDATE productos " +
            "SET stock = :cantidad ,"+
            "fecha_modificacion = NOW() "+
            "WHERE id = :id", nativeQuery = true)
    void modificarStock(@Param(value = "id") long id,@Param(value = "cantidad") int cantidad);

    @Transactional
    @Modifying
    @Query(value = "UPDATE productos " +
            "SET destacado = :destacado ,"+
            "fecha_modificacion = NOW() "+
            "WHERE id = :id", nativeQuery = true)
    void modificarDestacado(@Param(value = "id") long id,@Param(value = "destacado") boolean destacado);
}
