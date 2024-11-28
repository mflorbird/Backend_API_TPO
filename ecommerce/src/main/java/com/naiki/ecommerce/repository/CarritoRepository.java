package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository <Carrito, Long>{
    List<Carrito> findByUserId(Long userId);

}

//findAll(): obtener carritos
//findById(long id): busca el carrito x ID
//save(Carrito ccarito): guarda o atualizael carro en BD
//DELETE(Carrito carrito): elimina carrito