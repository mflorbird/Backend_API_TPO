package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoFavoritoRepository;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.ProductoVisitadoRepository;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.User;
import com.naiki.ecommerce.repository.entity.Producto;
import com.naiki.ecommerce.repository.entity.ProductoFavorito;
import com.naiki.ecommerce.repository.entity.ProductoVisitado;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CatalogoProductosService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoVisitadoRepository productoVisitadoRepository;

    @Autowired
    private ProductoFavoritoRepository productoFavoritoRepository;

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

}
