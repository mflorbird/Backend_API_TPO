package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long>  {

}
