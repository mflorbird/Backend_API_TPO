package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.ProductoVisitadoRepository;
import com.naiki.ecommerce.repository.UserRepository;
import com.naiki.ecommerce.repository.entity.Producto;
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


    // Agregar un producto al carrito (Ver si se superpone)
    public String agregarProductoAlCarrito(String productoId) {
        return "Agregar un producto al carrito: " + productoId;
    }

    // Agregar a Favoritos del usuario
    public String agregarProductoAFavoritos(String userId, String productoId) {
        return "Agregar a Favoritos del usuario: " + userId + " - " + productoId;
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

}
