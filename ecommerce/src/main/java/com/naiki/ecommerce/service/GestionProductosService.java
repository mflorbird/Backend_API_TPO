package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class GestionProductosService {

    private ProductoRepository productoRepository;

    public GestionProductosService(){
        this.productoRepository = new ProductoRepository();
    }

    public void crearProducto() throws Exception{

    }


}
