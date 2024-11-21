package com.naiki.ecommerce.service;

import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionProductosService {

    @Autowired
    private ProductoRepository productoRepository;

//    public void altaProducto(String nombre, String categoria, String descripcion, String foto, boolean destacado, String estado,Double precio, int stock) {
//        Producto producto = new Producto();
//        producto.setNombre(nombre);
//        producto.setCategoria(categoria);
//        producto.setDescripcion(descripcion);
//        producto.setImagen(foto);
//        producto.setDestacado(destacado);
//        producto.setEstado(estado);
//        producto.setPrecio(precio);
//        producto.setStock(stock);
//        productoRepository.save(producto);
//    }

    //    public void modificarStockProducto(Long id, int cantidad){
//        productoRepository.modificarStock(id, cantidad);
//    }
//
//    public void modificarDestacadoProducto(Long id, boolean destacado){
//        productoRepository.modificarDestacado(id, destacado);
//    }
//
//    public void bajaProducto(Long id) {
//        productoRepository.deleteById(id);
//    }
//
//    public Producto getProducto(Long id) {
//        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//        return producto;
//    }
//}

    public void altaProducto(String model, String category, String description, String image, boolean featured, String estado, Double price, List<Producto.Stock> stockTotal) {
        Producto producto = new Producto();
        producto.setModel(model);
        producto.setCategory(category);
        producto.setDescription(description);
        producto.setImage(image);
        producto.setFeatured(featured);
        producto.setEstado(estado);
        producto.setPrice(price);
        producto.setStockTotal(stockTotal);
        productoRepository.save(producto);
    }

    public void modificarStockProducto(Long id, int cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(cantidad);
        productoRepository.save(producto);
    }

    public void modificarDestacadoProducto(Long id, boolean destacado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setDestacado(destacado);
        productoRepository.save(producto);
    }

    public void bajaProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto getProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
}
