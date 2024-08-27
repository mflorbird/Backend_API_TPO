package com.naiki.ecommerce.controllers;

import com.naiki.ecommerce.service.CatalogoProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping ("api/v1/gestionCatalogo")
public class CatalogoProductosController {

    @Autowired
    private CatalogoProductosService service;

    // Productos destacados
    @RequestMapping("/productosDestacados")
    public String getProductosDestacados() {
        return service.getProductosDestacados();
    }

    // Productos por categoria
    @RequestMapping("/productosPorCategoria/{categoria}")
    public String getProductosPorCategoria(@PathVariable String categoria) {
        return service.getProductosPorCategoria(categoria);
    }

    // Productos recientes vistos por el usuario
    @RequestMapping("/productosRecientes")
    public String getProductosRecientes() {
        return service.getProductosRecientes();
    }

    // Detalle de un producto
    @RequestMapping("/detalleProducto/{productoId}")
    public String getDetalleProducto(@PathVariable String productoId) {
        return service.getDetalleProducto(productoId);
    }

    // Revisar stock de un producto
    @RequestMapping("/stockProducto/{productoId}")
    public String getStockProducto(@PathVariable String productoId) {
        return service.getStockProducto(productoId);
    }

    // Agregar un producto al carrito (Ver si se superpone)
    @RequestMapping("/agregarProductoAlCarrito/{productoId}")
    public String agregarProductoAlCarrito(@PathVariable String productoId) {
        return service.agregarProductoAlCarrito(productoId);
    }

    // Agregar a Favoritos del usuario
    @RequestMapping("/agregarProductoAFavoritos/{userId}/{productoId}")
    public String agregarProductoAFavoritos(@PathVariable String userId, @PathVariable String productoId) {
        return service.agregarProductoAFavoritos(userId, productoId);
    }

}
