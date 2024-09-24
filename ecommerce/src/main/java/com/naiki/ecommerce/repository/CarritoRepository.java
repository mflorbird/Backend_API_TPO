package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository <Carrito, Long>{

}

//findAll(): obtener carritos
//findById(long id): busca el carrito x ID
//save(Carrito ccarito): guarda o atualizael carro en BD
//DELETE(Carrito carrito): elimina carrito