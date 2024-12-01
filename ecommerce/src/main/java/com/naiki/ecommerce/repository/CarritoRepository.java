package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository <Carrito, Long>{
    List<Carrito> findByUserId(Long userId);
    List<Carrito> findByUserIdAndEstado(Long userId, String estado);

}