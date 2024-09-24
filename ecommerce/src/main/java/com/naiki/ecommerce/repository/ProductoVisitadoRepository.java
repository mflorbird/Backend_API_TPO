package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.ProductoVisitado;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoVisitadoRepository extends JpaRepository<ProductoVisitado, Long> {
    List<ProductoVisitado> findTop10ByUserEmailOrderByFechaVisitaDesc(String email);

//    @Query("SELECT DISTINCT pv.producto FROM ProductoVisitado pv " +
//            "WHERE pv.user.email = :email " +
//            "AND pv.fechaVisita = (SELECT MAX(pv2.fechaVisita) FROM ProductoVisitado pv2 " +
//            "                      WHERE pv2.producto = pv.producto AND pv2.user.email = :email) " +
//            "ORDER BY pv.fechaVisita DESC")
//    List<ProductoVisitado> findDistincProductosByUserEmailOrderByFechaVisitaDesc(
//            @Param("email") String email, Pageable pageable);
 }
