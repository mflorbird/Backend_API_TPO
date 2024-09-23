package com.naiki.ecommerce.controllers.carrito;

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

    @GetMapping // para tener todos los carritos
    public List<Carrito> getAllCarritos() {
        return carritoService.findAll(); //->
    }

    @GetMapping("/{id}")  // obtener carrito por id
    public ResponseEntity<Carrito> getCarritoById(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carrito);
    }

    @PostMapping // crear nuevo carro
    public ResponseEntity<Carrito> createCarrito(@RequestBody Carrito carrito) {
        Carrito newCarrito = carritoService.save(carrito);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCarrito);
    }

    @PostMapping("/{carritoId}/productos") // agrega producto al carrito
    public ResponseEntity<Carrito> addProductoToCarrito(@PathVariable Long carritoId, @RequestBody Producto producto, @RequestParam int cantidad) {
        try {
            Carrito carrito = carritoService.agregarProductoAlCarrito(carritoId, producto, cantidad);
            return ResponseEntity.ok(carrito);
        } catch (SinStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{carritoId}/productos/{productoId}") // elimina producto del carrito
    public ResponseEntity<Void> removeProductoFromCarrito(@PathVariable Long carritoId, @PathVariable Long productoId) {
        carritoService.eliminarProductoDelCarrito(carritoId, productoId);
        return ResponseEntity.noContent().build();
    }
}
