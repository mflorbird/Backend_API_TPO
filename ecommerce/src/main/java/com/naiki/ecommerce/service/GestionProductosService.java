package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestionProductosService {

    @Autowired
    private ProductoRepository productoRepository;

    public void altaProducto(String nombre, String categoria, String descripcion) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setDescripcion(descripcion);
        productoRepository.save(producto);
    }

    public void modificarStockProducto(Long id, int cantidad){
        productoRepository.modificarStock(id, cantidad);
    }

    public void bajaProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto getProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return producto;
    }
}
