package com.naiki.ecommerce.controllers.carrito;

import com.naiki.ecommerce.exception.SinStockException;
import com.naiki.ecommerce.repository.entity.*;
import com.naiki.ecommerce.service.*;
import com.naiki.ecommerce.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Carrito carritoCreado = carritoService.createCarrito();
        return ResponseEntity.ok(carritoCreado);
    }

    @PostMapping("/agregarProducto") // agrega producto al carrito
    public ResponseEntity<Carrito> addProductoToCarrito(@RequestBody ProductoRequest productoRequest, @RequestHeader("Authorization") String token) {
        Long productoId = parseLong(productoRequest.getProductoId());
        try {
            Carrito carrito = carritoService.agregarProductoAlCarrito(productoId, productoRequest.getCantidad());
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

    @GetMapping("/obtenerCarrito")
    public ResponseEntity<?> obtenerCarrito(@RequestHeader("Authorization") String token){
        System.out.println("Token recibido: " + token); // Agregar log para saber si recibe ok.
        Carrito carrito = carritoService.obtenerCarritoUsuario();

        //si el carrito es null, no hay carrito creado para el usuario
        if (carrito == null){
            return ResponseEntity.ok("Aun no tenes productos en el carrito (acá no existe porque nunca se agrego un producto)");
        }

        //si el carrito no tiene productos
        if (carrito.getItems()== null || carrito.getItems().isEmpty()){
            return ResponseEntity.ok("Aún no tenes productos en el carrito. (acá existe pero no tiene productos)");
        }

        //si el carrito tiene producto, devuelve elcarrito completo
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/descuento")
    public ResponseEntity<?> aplicarDescuento(@RequestBody DescuentoRequest descuentoRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("Usuario no autenticado");
            }
            Carrito carritoConDescuento = carritoService.aplicarDescuentoAlCarrito(descuentoRequest.getCodigoDescuento());
            double descuentoAplicado = carritoConDescuento.getTotalOriginal() * carritoConDescuento.getPorcentajeDescuentoAplicado();
            return ResponseEntity.ok("Total Original: $" + carritoConDescuento.getTotalOriginal() + ", Descuento aplicado: $" + descuentoAplicado + ", Precio Final: $" + carritoConDescuento.getTotalPrecio());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<?> realizarCheckout(@PathVariable Long id) {
        try {
            carritoService.realizarCheckout();
            return ResponseEntity.ok("Checkout realizado con éxito");
        } catch (SinStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<Carrito> cerrarCarrito(@PathVariable Long id) {
        try{
            Carrito carritoCerrado = carritoService.cerrarCarrito(id);
            return ResponseEntity.ok(carritoCerrado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}