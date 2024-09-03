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
    public String getProductosDestacados() {
        return "Productos destacados";
    }

    // Productos por categoria
    public String getProductosPorCategoria(String categoria) {
        return "Productos por categoria: " + categoria;
    }

    // Productos recientes vistos por el usuario
    public String getProductosRecientes() {
        return "Productos recientes";
    }

    // Detalle de un producto
    public Producto getDetalleProducto(String productoId) {
        Producto producto =  productoRepository.findById(Long.valueOf(productoId)).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return producto;
    }

    // Revisar stock de un producto
    public String getStockProducto(String productoId) {
        return "Revisar stock de un producto: " + productoId;
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
