package com.naiki.ecommerce.repository;

import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepository {

    private List<Producto> listProducto = new ArrayList<>();

    public ProductoRepository(){
        listProducto.add(new Producto("Air Max","Urbano"));
    }

}
