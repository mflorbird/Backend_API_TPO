package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.service.CatalogoProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("api/v1/gestionCatalogo")
public class CatalogoProductosController {

    @Autowired
    private CatalogoProductosService catalogoProductosService;

    // Productos todos
    @GetMapping("/") //tested ok
    public ResponseEntity<?> getProductos() {
        return ResponseEntity.ok(catalogoProductosService.getProductos());
    }

    @GetMapping("/productos/{productoId}") //tested ok
    public ResponseEntity<Producto> getDetalleProducto(@PathVariable("productoId") Long productoId) {
        Producto producto = catalogoProductosService.getDetalleProducto(productoId);

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String email = authentication.getName();
//            catalogoProductosService.agregarProductoVisitado(email, productoId);
//        }

        return ResponseEntity.ok(producto);
    }

    @GetMapping("/productos/recientes")
    public ResponseEntity<List<Producto>> getProductosRecientes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            List<Producto> productos = catalogoProductosService.getProductosRecientes(email);
            return ResponseEntity.ok(productos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/productos/destacados")
    public ResponseEntity<List<Producto>> getProductosDestacados() {
        try {
            return ResponseEntity.ok(catalogoProductosService.getProductosDestacados());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


@GetMapping("/productos/favoritos")
public ResponseEntity<List<Producto>> getProductosFavoritos() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                List<Producto> productos = catalogoProductosService.getProductosFavoritos(email);
                return ResponseEntity.ok(productos);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PatchMapping("/favoritos")
    public ResponseEntity<?> updateFavorites(@RequestBody List<Producto> nuevosFavoritos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Boolean res = catalogoProductosService.updateFavorites(email, nuevosFavoritos);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PatchMapping("/visitados")
    public ResponseEntity<?> updateVisitados(@RequestBody List<Producto> nuevosVisitados) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Boolean res = catalogoProductosService.updateVisitados(email, nuevosVisitados);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


}
