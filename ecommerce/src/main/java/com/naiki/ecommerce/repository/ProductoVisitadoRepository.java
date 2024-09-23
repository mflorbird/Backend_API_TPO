package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.ProductoVisitado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoVisitadoRepository extends JpaRepository<ProductoVisitado, Long> {
    List<ProductoVisitado> findTop10ByUserEmailOrderByFechaVisitaDesc(String email);
}
