package com.naiki.ecommerce.service;

import com.naiki.ecommerce.dto.ProductRequest;
import com.naiki.ecommerce.repository.ProductoRepository;
import com.naiki.ecommerce.repository.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GestionProductosService {

    @Autowired
    private ProductoRepository productoRepository;

    public void altaProducto(ProductRequest productRequest) {
        Producto producto = new Producto();
        System.out.println(UUID.randomUUID());
        crearProducto(productRequest, producto);
    }

    public void modificarProducto(Long id, ProductRequest productRequest) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        crearProducto(productRequest, producto);
    }


    private void crearProducto(ProductRequest productRequest, Producto producto) {
        System.out.println("ProductRequest: " + productRequest.toString());
        System.out.println("Creando producto: " + producto.toString());

        if (productRequest.getNombre() != null) {
            producto.setNombre(productRequest.getNombre());
        }
        if (productRequest.getDescripcion() != null) {
            producto.setDescripcion(productRequest.getDescripcion());
        }
        if (productRequest.getCategoria() != null) {
            producto.setCategoria(productRequest.getCategoria());
        }
        if (productRequest.getPrecio() != null) {
            producto.setPrecio(productRequest.getPrecio());
        }
        if (productRequest.getStockTotal() != null) {
            List<SizeStock> stockTotal = productRequest.getStockTotal().stream()
                    .map(sizeStock -> {
                        SizeStock sizeStockEntity = new SizeStock();
                        sizeStockEntity.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
                        sizeStockEntity.setCantidad(Integer.parseInt(sizeStock.getCantidad()));
                        sizeStockEntity.setTalle(sizeStock.getTalle());
                        return sizeStockEntity;
                    }).toList();
            producto.setStockTotal(stockTotal);
        }
        if (productRequest.getImagen() != null) {
            producto.setImagen(productRequest.getImagen());
        }
        if (productRequest.getDestacado() != null) {
            producto.setDestacado(Boolean.parseBoolean(productRequest.getDestacado()));
        }

        System.out.println("Guardando producto: " + producto.toString());
        productoRepository.save(producto);
    }


    public void bajaProducto(Long id) {
        productoRepository.deleteById(id);
    }


}