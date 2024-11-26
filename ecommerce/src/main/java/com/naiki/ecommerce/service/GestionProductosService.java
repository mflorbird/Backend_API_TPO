package com.naiki.ecommerce.service;

import com.naiki.ecommerce.dto.ProductRequest;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestionProductosService {

    @Autowired
    private ProductoRepository productoRepository;

    public void altaProducto(ProductRequest productRequest) {
        Producto producto = new Producto();
        crearProducto(productRequest, producto);
    }

    public void modificarProducto(Long id, ProductRequest productRequest) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        crearProducto(productRequest, producto);
    }

    private void crearProducto(ProductRequest productRequest, Producto producto) {
        producto.setNombre(productRequest.getNombre());
        producto.setDescripcion(productRequest.getDescripcion());
        producto.setPrecio(productRequest.getPrecio());
        producto.setCategoria(productRequest.getCategoria());
        producto.setStockTotal(productRequest.getStockTotal());
        producto.setDestacado(productRequest.isDestacado());
        producto.setImagen(productRequest.getImagen());
        productoRepository.save(producto);
    }
    public void bajaProducto(Long id) {
        productoRepository.deleteById(id);
    }




//    public void modificarDestacadoProducto(Long id, boolean destacado){
//        productoRepository.modificarDestacado(id, destacado);
//    }



    public Producto getProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return producto;
    }
}
