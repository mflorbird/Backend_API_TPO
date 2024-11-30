package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.*;
import com.naiki.ecommerce.repository.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CatalogoProductosService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UserRepository userRepository;

    // Productos todos
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    // Detalle de un producto
    public Producto getDetalleProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado para ID: " + productoId));
    }



    // Productos recientes vistos por el usuario
    public List<Producto> getProductosRecientes(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para email: " + email));
        List<Integer> visitados = user.getVisitados();
        if (visitados == null) {
            return new ArrayList<>();
        }
        return productoRepository.findByIdIn(visitados);
    }


    // Productos destacados
    public List<Producto> getProductosDestacados() {
        return productoRepository.findByDestacado(true);
    }

    public List<Producto> getProductosFavoritos(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getFavoritos() == null) {
            return new ArrayList<>();
        }
        return productoRepository.findByIdIn(user.getFavoritos());
    }

    // Agregar un producto a la lista de productos visitados
    public User updateVisitados(String email, List<Integer> nuevosVisitados) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para email: " + email));
            user.setVisitados(nuevosVisitados);
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User updateFavorites(String email, List<Integer> nuevosFavoritos) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para email: " + email));
            System.out.println("Favoritos: " + nuevosFavoritos);
            System.out.println("User: " + user);
            user.setFavoritos(nuevosFavoritos);
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<ProductoDisponible> getProductosDisponibles(String email) {
        // Buscar el carrito de compras del usuario
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para email: " + email));

            Carrito carrito = carritoRepository.findByUserIdAndEstado(user.getId(), "activo");

            List<ProductoDisponible> productosDisponibles = new ArrayList<>();

            // Buscar en los items del carrito
            for (String key : carrito.getItems().keySet()) {
                ProductoDisponible productoDisponible = new ProductoDisponible();
                productoDisponible.setItemId(key);
                productoDisponible.setCarritoId(String.valueOf(carrito.getId()));

                String[] keyParts = key.split("---");
                String productoId = keyParts[0];
                String talle = keyParts[1];

                productoDisponible.setProductoId(productoId);

                // Buscar el producto en la base de datos
                Optional<Producto> producto = productoRepository.findById(Long.valueOf(productoId));

                if (producto.isEmpty()) {
                    productoDisponible.setDisponible(false);
                } else {
                    productoDisponible.setDisponible(true);

                    for (SizeStock sizeStock : producto.get().getStockTotal()) {
                        if (sizeStock.getTalle().equals(talle)) {
                            productoDisponible.setCantidadMaxima(sizeStock.getCantidad());
                            break;
                        }
                    }
                }

                productosDisponibles.add(productoDisponible);
            }

            return productosDisponibles;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }
}

