package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.GestionProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;

@RestController
@RequestMapping ("api/v1/gestionProductos")
public class GestionProductosController {

    @Autowired
    private GestionProductosService gestionProductosService;

    // Have to add the photo to the POST
    @PostMapping ("/crearProducto")
    public void altaProducto(String descripcion, String categoria) throws Exception{

    }

    @DeleteMapping ("/eliminarProducto")
    public void bajaProducto() throws Exception{

    }

}
