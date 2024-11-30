package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.repository.entity.ProductoDisponible;
import com.naiki.ecommerce.repository.entity.User;
import com.naiki.ecommerce.service.CatalogoProductosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("api/v1/gestionCatalogo")
public class CatalogoProductosController {

    @Autowired
    private CatalogoProductosService catalogoProductosService;

    // Productos todos
    @GetMapping("/") //tested ok
    public ResponseEntity<List<Producto>> getProductos() {
    try {
        System.out.println("Obteniendo productos");
        return ResponseEntity.ok(catalogoProductosService.getProductos());

    } catch (Exception e) {
        System.out.println("Error al obtener productos: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
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
    public ResponseEntity<?> updateFavorites(@RequestBody Map<String, Object> requestBody) {
        List<Integer> nuevosFavoritos = (List<Integer>) requestBody.get("favoritos");
        System.out.println("Actualizando favoritos" + nuevosFavoritos);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = catalogoProductosService.updateFavorites(email, nuevosFavoritos);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/visitados")
    public ResponseEntity<?> updateVisitados(@RequestBody Map<String, Object> requestBody) {
        List<Integer> nuevosVisitados = (List<Integer>) requestBody.get("visitados");
        System.out.println("Actualizando visitados" + nuevosVisitados);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = catalogoProductosService.updateVisitados(email, nuevosVisitados);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ProductoDisponible>> getProductosDisponibles() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = authentication.getName();
            List<ProductoDisponible> productosDisponibles = catalogoProductosService.getProductosDisponibles(email);

            return ResponseEntity.ok(productosDisponibles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Log the actual error for server-side debugging
            System.err.println("Error al obtener productos disponibles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
