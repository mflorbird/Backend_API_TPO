package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.GestionProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping ("api/v1/gestionProductos")
public class GestionProductosController {

    @Autowired
    private GestionProductosService gestionProductosService;

    @PostMapping ("/crearProducto")
    public ResponseEntity<?> altaProducto(@RequestParam("nombre") String nombre, @RequestParam("categoria") String categoria, @RequestParam("descripcion") String descripcion,@RequestParam("foto") String foto, @RequestParam("destacado") boolean destacado, @RequestParam("estado") String estado, @RequestParam("precio") Double precio, @RequestParam("stock") int stock) {
        try{
            gestionProductosService.altaProducto(nombre, categoria, descripcion, foto, destacado,estado, precio,stock);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/modificarStockProducto/")
    public ResponseEntity<?> modificarStockProducto(@RequestParam("productoId") Long productoId, @RequestParam("cantidad") int cantidad) {
        try{
            gestionProductosService.modificarStockProducto(productoId, cantidad);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/modificarDestacadoProducto/")
    public ResponseEntity<?> modificarDestacadoProducto(@RequestParam("productoId") Long productoId, @RequestParam("destacado") boolean destacado) {
        try{
            gestionProductosService.modificarDestacadoProducto(productoId, destacado);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping ("/eliminarProducto/{productoId}")
    public void bajaProducto(@PathVariable("productoId") Long productoId){
        try{
            gestionProductosService.bajaProducto(productoId);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
