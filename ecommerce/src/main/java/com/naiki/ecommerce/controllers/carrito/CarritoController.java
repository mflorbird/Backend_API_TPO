package com.naiki.ecommerce.controllers.carrito;

import com.naiki.ecommerce.repository.entity.*;
import com.naiki.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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

    @PostMapping("/{carritoId}/productos") // agrega producto al carrito
    public ResponseEntity<Carrito> addProductoToCarrito(@PathVariable Long carritoId, @RequestBody Producto producto, @RequestParam int cantidad) {
       try {
           Carrito carrito = carritoService.agregarProductoAlCarrito(carritoId, producto, cantidad);
            return ResponseEntity.ok(carrito);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @DeleteMapping("/eliminarProducto") // elimina producto del carrito
    public ResponseEntity<Void> removeProductoFromCarrito(@RequestBody Long productoId, @RequestBody Long carritoId, @RequestBody int cantidad) {
        carritoService.eliminarProductoDelCarrito(carritoId, productoId, cantidad);
        return ResponseEntity.noContent().build();
    }
}
