package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.ProductoVisitado;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoVisitadoRepository extends JpaRepository<ProductoVisitado, Long> {
    List<ProductoVisitado> findTop10ByUserEmailOrderByFechaVisitaDesc(String email);

 }
