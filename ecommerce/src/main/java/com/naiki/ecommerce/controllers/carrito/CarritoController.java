package com.naiki.ecommerce.controllers.carrito;

import com.naiki.ecommerce.exception.SinStockException;
import com.naiki.ecommerce.repository.entity.*;
import com.naiki.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// aca hay que validar la informacion del mensaje.
@RestController //este es el que maneja las solicitudes https y devuelve JSON
@RequestMapping("/api/carritos") // esta es la ruta
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
    public ResponseEntity<Carrito> addProductoToCarrito(@RequestBody Long productoId, @RequestBody int cantidad, @RequestBody Long carritoId)  {
        try {
            Carrito carrito = carritoService.agregarProductoAlCarrito(carritoId, productoId, cantidad);
            return ResponseEntity.ok(carrito);
        } catch (SinStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/eliminarProducto") // elimina producto del carrito
    public ResponseEntity<Void> removeProductoFromCarrito(@RequestBody Long productoId, @RequestBody Long carritoId, @RequestBody int cantidad) {
        carritoService.eliminarProductoDelCarrito(carritoId, productoId, cantidad);
        return ResponseEntity.noContent().build();
    }
}
