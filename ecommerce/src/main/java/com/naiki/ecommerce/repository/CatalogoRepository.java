package com.naiki.ecommerce.repository;

import org.springframework.stereotype.Repository;

@Repository
public class CatalogoRepository {

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
        public String getDetalleProducto(String productoId) {
            return "Detalle de un producto: " + productoId;
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

