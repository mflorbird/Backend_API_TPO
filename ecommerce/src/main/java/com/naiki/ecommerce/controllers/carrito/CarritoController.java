package com.naiki.ecommerce.controllers.carrito;

import com.naiki.ecommerce.exception.SinStockException;
import com.naiki.ecommerce.repository.entity.*;
import com.naiki.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

import static java.lang.Long.parseLong;

// aca hay que validar la informacion del mensaje.
@RestController //este es el que maneja las solicitudes https y devuelve JSON
@RequestMapping("/api/v1/carritos") // esta es la ruta
public class CarritoController {

    @Autowired // para que el controller use los metodos de service
    private CarritoService carritoService;

    @GetMapping("/all")
    public List<Carrito> getAllCarritos() {
        return carritoService.findAll();
    }

    @GetMapping("/{id}")  // obtener carrito por id
    public ResponseEntity<Carrito> getCarritoById(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/create") // crea un carrito
    public ResponseEntity<Carrito> createCarrito(@RequestHeader("Authorization") String token) {
        Carrito carritoCreado = carritoService.createCarrito(token);
        return ResponseEntity.ok(carritoCreado);
    }

    @PostMapping("/agregarProducto") // agrega producto al carrito
    public ResponseEntity<Carrito> addProductoToCarrito(@RequestBody ProductoRequest productoRequest) {
        Long carritoId = parseLong(productoRequest.getCarritoId());
        Long productoId = parseLong(productoRequest.getProductoId());
        try {
            Carrito carrito = carritoService.agregarProductoAlCarrito(carritoId, productoId, productoRequest.getCantidad());
            return ResponseEntity.ok(carrito);
        } catch (SinStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/eliminarProducto") // elimina producto del carrito
    public ResponseEntity<Void> removeProductoFromCarrito(@RequestBody ProductoRequest productoRequest) {
        Long carritoId = parseLong(productoRequest.getCarritoId());
        Long productoId = parseLong(productoRequest.getProductoId());
        int cantidad = productoRequest.getCantidad();
        carritoService.eliminarProductoDelCarrito(carritoId, productoId, cantidad);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciarCarrito")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId) {
        boolean resultado = carritoService.vaciarCarrito(carritoId);
        if (resultado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

