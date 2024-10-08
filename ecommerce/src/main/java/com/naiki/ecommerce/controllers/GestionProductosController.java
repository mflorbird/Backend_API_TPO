package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.dto.ProductoDTO;
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

    @PostMapping ("/productos")
    public ResponseEntity<?> altaProducto(@RequestBody ProductoDTO productoDTO) {
        try{
            gestionProductosService.altaProducto(  productoDTO.getNombre(),
                    productoDTO.getCategoria(),
                    productoDTO.getDescripcion(),
                    productoDTO.getFoto(),
                    productoDTO.isDestacado(),
                    productoDTO.getEstado(),
                    productoDTO.getPrecio(),
                    productoDTO.getStock() );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/productos/{productoId}/stock")
    public ResponseEntity<?> modificarStockProducto(@RequestParam("productoId") Long productoId, @RequestParam("cantidad") int cantidad) {
        try{
            gestionProductosService.modificarStockProducto(productoId, cantidad);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/productos/{productoId}/destacado")
    public ResponseEntity<?> modificarDestacadoProducto(@RequestParam("productoId") Long productoId, @RequestParam("destacado") boolean destacado) {
        try{
            gestionProductosService.modificarDestacadoProducto(productoId, destacado);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping ("/productos/{productoId}")
    public void bajaProducto(@PathVariable("productoId") Long productoId){
        try{
            gestionProductosService.bajaProducto(productoId);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
