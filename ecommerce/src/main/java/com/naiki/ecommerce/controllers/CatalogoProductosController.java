package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.service.CatalogoProductosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    // Productos destacados
    @GetMapping("/productos/destacados") //tested ok
    public ResponseEntity<?> getProductosDestacados() {
        return ResponseEntity.ok(catalogoProductosService.getProductosDestacados());
    }

    // Productos por categoria
    @GetMapping("/productos/categoria/{categoria}") //tested ok
    public ResponseEntity<List<Producto>> getProductosPorCategoria(@PathVariable("categoria") String categoria) {
        List<Producto> productos = catalogoProductosService.getProductosPorCategoria(categoria);
        if (productos == null || productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        return ResponseEntity.ok(productos);
    }

    // Detalle de un producto
    @GetMapping("/productos/{productoId}") //tested ok
    public ResponseEntity<Producto> getDetalleProducto(@PathVariable("productoId") Long productoId) {
        Producto producto = catalogoProductosService.getDetalleProducto(productoId);

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            catalogoProductosService.agregarProductoVisitado(email, productoId);
        }

        return ResponseEntity.ok(producto);
    }

    // Revisar stock de un producto
//    @GetMapping("/productos/{productoId}/stock") //tested ok
//    public ResponseEntity<?> getStockProducto(@PathVariable("productoId") Long productoId) {
//        try {
//            int stock = catalogoProductosService.getStockProducto(productoId);
//            return ResponseEntity.ok(stock);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Producto no encontrado para ID: " + productoId);
//        }
//    }

    // Agregar a Favoritos del usuario
    @PutMapping("/productos/favoritos/{productoId}")
    public ResponseEntity<String> agregarProductoAFavoritos(@PathVariable("productoId") Long productoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String res = catalogoProductosService.agregarProductoFavorito(email, productoId);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping("/productos/favoritos/{productoId}")
    public ResponseEntity<String> eliminarProductoFavorito(@PathVariable("productoId") Long productoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String res = catalogoProductosService.eliminarProductoFavorito(email, productoId);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/productos/favoritos")
    public ResponseEntity<List<Producto>> getProductosFavoritos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            List<Producto> productos = catalogoProductosService.getProductosFavoritos(email);
            return ResponseEntity.ok(productos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Productos recientes vistos por el usuario
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
}
