package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.GestionProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;

@RestController
@RequestMapping ("api/v1/gestionProductos")
public class GestionProductosController {

    @Autowired
    private GestionProductosService gestionProductosService;

    // Have to add the photo to the POST
    @PostMapping ("/crearProducto")
    public ResponseEntity<?> altaProducto(@RequestParam("nombre") String nombre, @RequestParam("categoria") String categoria, @RequestParam("descripcion") String descripcion) {
        gestionProductosService.altaProducto(nombre, categoria, descripcion);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modificarStockProducto/{productoId}")
    public ResponseEntity<?> modificarStockProducto(@RequestParam("productoId") Long productoId, @RequestParam("cantidad") int cantidad) {
        gestionProductosService.modificarStockProducto(productoId, cantidad);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping ("/eliminarProducto/{productoId}")
    public void bajaProducto(@RequestParam("productoId") Long productoId) throws Exception{
        gestionProductosService.bajaProducto(productoId);
    }

}
