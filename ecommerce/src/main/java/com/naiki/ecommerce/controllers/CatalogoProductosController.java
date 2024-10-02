package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.controllers.config.JwtService;
import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.service.CatalogoProductosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping ("api/v1/gestionCatalogo")
public class CatalogoProductosController {

    @Autowired
    private CatalogoProductosService catalogoProductosService;

    @Autowired
    private JwtService jwtService;

    // Productos todos
    @GetMapping("/productos") //tested ok
    public ResponseEntity<?> getProductos() {
        return ResponseEntity.ok(catalogoProductosService.getProductos());
    }

    // Productos destacados
    @GetMapping("/productosDestacados") //tested ok
    public ResponseEntity<?> getProductosDestacados() {
        return ResponseEntity.ok(catalogoProductosService.getProductosDestacados());
    }

    // Productos por categoria
    @GetMapping("/productosPorCategoria/{categoria}") //tested ok
    public ResponseEntity<List<Producto>> getProductosPorCategoria(@PathVariable("categoria") String categoria) {
        List<Producto> productos = catalogoProductosService.getProductosPorCategoria(categoria);
        if (productos == null || productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        return ResponseEntity.ok(productos);
    }


    // Detalle de un producto
    @GetMapping("/detalleProducto/{productoId}") //tested ok
    public ResponseEntity<Producto> getDetalleProducto(@PathVariable("productoId") Long productoId,
                                                       @RequestHeader(value = "Authorization", required = false) String token) {
        Producto producto = catalogoProductosService.getDetalleProducto(productoId);

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        if (token != null) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).strip();
            }
            String email = jwtService.extractUsername(token);
            catalogoProductosService.agregarProductoVisitado(email, productoId);

        }

        return ResponseEntity.ok(producto);
    }


    // Revisar stock de un producto
    @GetMapping("/stockProducto/{productoId}") //tested ok
    public ResponseEntity<?> getStockProducto(@PathVariable("productoId") Long productoId) {
        try {
            int stock = catalogoProductosService.getStockProducto(productoId);
            return ResponseEntity.ok(stock);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado para ID: " + productoId);
        }
    }


    // Agregar a Favoritos del usuario
    @PutMapping("/agregarProductoAFavoritos/")
    public ResponseEntity<String> agregarProductoAFavoritos(@RequestHeader(value = "Authorization") String token,
                                            @RequestParam("productoId") Long productoId) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).strip();
            }
            String email = jwtService.extractUsername(token);
            String res = catalogoProductosService.agregarProductoFavorito(email, productoId);
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/eliminarProductoFavorito/")
    public ResponseEntity<String> eliminarProductoFavorito(@RequestHeader(value = "Authorization") String token,
                                            @RequestParam("productoId") Long productoId) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).strip();
            }
            String email = jwtService.extractUsername(token);
            String res = catalogoProductosService.eliminarProductoFavorito(email, productoId);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/productosFavoritos")
    public ResponseEntity<List<Producto>> getProductosFavoritos(
            @RequestHeader(value = "Authorization") String token)
    {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).strip();
            }
            String email = jwtService.extractUsername(token);
            List<Producto> productos = catalogoProductosService.getProductosFavoritos(email);
            return ResponseEntity.ok(productos);
        }
    }

    // Productos recientes vistos por el usuario

    @GetMapping("/productosRecientes")
    public ResponseEntity<List<Producto>> getProductosRecientes(
            @RequestHeader(value = "Authorization") String token)
    {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).strip();
            }
            String email = jwtService.extractUsername(token);
            List<Producto> productos = catalogoProductosService.getProductosRecientes(email);
            return ResponseEntity.ok(productos);
        }
    }

}
