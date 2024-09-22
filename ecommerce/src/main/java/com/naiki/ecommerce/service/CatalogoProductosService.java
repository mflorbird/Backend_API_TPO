package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoProductosService {

    @Autowired
    private ProductoRepository productoRepository;

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

    // Productos recientes vistos por el usuario
    public String getProductosRecientes() {
        return "Productos recientes";
    }

    // Detalle de un producto
    public Producto getDetalleProducto(String productoId) {
        return productoRepository.findById(Long.parseLong(productoId)).orElse(null);
    }

    // Revisar stock de un producto
    public int getStockProducto(String productoId) {
        Producto producto = productoRepository.findById(Long.parseLong(productoId)).orElse(null);
        return producto != null ? producto.getStock() : -1;
    }

    // Agregar un producto al carrito (Ver si se superpone)
    public String agregarProductoAlCarrito(String productoId) {
        return "Agregar un producto al carrito: " + productoId;
    }

    // Agregar a Favoritos del usuario
    public String agregarProductoAFavoritos(String userId, String productoId) {
        return "Agregar a Favoritos del usuario: " + userId + " - " + productoId;
    }

}
