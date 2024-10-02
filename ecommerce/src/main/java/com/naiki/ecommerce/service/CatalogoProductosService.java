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

    // Productos destacados
    public List<Producto> getProductosDestacados() {
        return productoRepository.findByDestacado(true);
    }

    // Productos por categoria
    public List<Producto> getProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }


    // Detalle de un producto
    public Producto getDetalleProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado para ID: " + productoId));
    }

    // Revisar stock de un producto
    public int getStockProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .map(Producto::getStock)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado para ID: " + productoId));
    }



    // Productos recientes vistos por el usuario
    public List<Producto> getProductosRecientes(String email) {
        List<ProductoVisitado> productosVisitados = productoVisitadoRepository.findTop10ByUserEmailOrderByFechaVisitaDesc(email);
        // elegir los primeros 5 productos distintos
        List<Producto> productosUnicos = new ArrayList<>();
        for (ProductoVisitado productoVisitado : productosVisitados) {
            Producto producto = productoVisitado.getProducto();
            if (!productosUnicos.contains(producto)) {
                productosUnicos.add(producto);
            }
            if (productosUnicos.size() == 5) {
                break;
            }

        }
        return productosUnicos;
    }

    // Agregar un producto a la lista de productos visitados
    public void agregarProductoVisitado(String email, Long productoId) {
        ProductoVisitado productoVisitado = new ProductoVisitado();
        productoVisitado.setUser(userRepository.findByEmail(email).orElseThrow());
        productoVisitado.setProducto(productoRepository.getOne(productoId));
        productoVisitado.setFechaVisita(new Date());
        productoVisitadoRepository.save(productoVisitado);
    }

    // Agregar un producto a la lista de favoritos
    public String agregarProductoFavorito(String email, Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();
        ProductoFavorito productoFavorito = productoFavoritoRepository.findByUserAndProducto(user, producto);

        if (productoFavorito != null) {
            productoFavorito.setFechaFavorito(new Date());
            productoFavoritoRepository.save(productoFavorito);
            return "Producto ya estaba en favoritos";
        }
        productoFavorito = new ProductoFavorito();
        productoFavorito.setUser(user);
        productoFavorito.setProducto(producto);
        productoFavorito.setFechaFavorito(new Date());
        productoFavoritoRepository.save(productoFavorito);
        return "Producto agregado a favoritos";
    }

    // Eliminar un producto de la lista de favoritos
    public String eliminarProductoFavorito(String email, Long productoId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Producto producto = productoRepository.getOne(productoId);
        ProductoFavorito productoFavorito = productoFavoritoRepository.findByUserAndProducto(user, producto);
        if (productoFavorito != null) {
            productoFavoritoRepository.delete(productoFavorito);
            return "Producto eliminado de favoritos";
        }
        return "Producto no estaba en favoritos";
    }

    public List<Producto> getProductosFavoritos(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<ProductoFavorito> productosFavoritos = productoFavoritoRepository.findByUserOrderByFechaFavoritoDesc(user);
        List<Producto> productos = new ArrayList<>();
        for (ProductoFavorito productoFavorito : productosFavoritos) {
            productos.add(productoFavorito.getProducto());
        }
        return productos;
    }
}
