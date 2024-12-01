package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ItemCarritoRepository extends JpaRepository <ItemCarrito, Long>{

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM item_carrito
            WHERE items_key = :itemKey
            AND carrito = :carritoId
            """, nativeQuery = true)
    void eliminarPorItemKeyAndCarritoId(@Param("itemKey") String itemKey, @Param("carritoId") Long carritoId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM item_carrito
            WHERE carrito = :carritoId
            """, nativeQuery = true)
    void eliminarPorCarritoId(@Param("carritoId") Long carritoId);


}