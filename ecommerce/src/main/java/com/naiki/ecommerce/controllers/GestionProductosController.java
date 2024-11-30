package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.dto.ProductRequest;
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
    public ResponseEntity<?> altaProducto(@RequestBody ProductRequest productRequest){
        try{
            gestionProductosService.altaProducto(productRequest);
            System.out.println("Producto creado");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            System.out.println("Error al crear producto" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/productos/{productoId}")
    public ResponseEntity<?> modificarProducto(
            @PathVariable("productoId") Long productoId,
            @RequestBody ProductRequest productRequest){
        try{
            System.out.println("Modificando producto");
            gestionProductosService.modificarProducto(productoId, productRequest);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            System.out.println("Error al modificar producto" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

//    {API_URL_PRODUCTO}/productos/${id}`);|
    @DeleteMapping ("/productos/{productoId}")
    public ResponseEntity<?> bajaProducto(@PathVariable("productoId") Long productoId){
        try{
            gestionProductosService.bajaProducto(productoId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }






//    @PutMapping("/productos/{productoId}/destacado")
//    public ResponseEntity<?> modificarDestacadoProducto(@RequestParam("productoId") Long productoId, @RequestParam("destacado") boolean destacado) {
//        try{
//            gestionProductosService.modificarDestacadoProducto(productoId, destacado);
//            return ResponseEntity.ok().build();
//        }catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//    }




}
