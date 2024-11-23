package com.naiki.ecommerce.dto;
import com.naiki.ecommerce.repository.entity.Producto;

import java.util.List;
public class ProductoDTO {

    //agrego esto y modifico porque en el front los tenemos en ingles. featured y list es nuevo asi los tenemos en el front.
    private String id;
    private String model;
    private String category;
    private String description;
    private String image;
    private boolean featured;
    private String estado;
    private Double price;
    private List<Stock> stockTotal;

    public ProductoDTO(String id, String model, String category, String description, String image, boolean featured, String estado, Double price, List<Stock> stockTotal) {
        this.id = id;
        this.model = model;
        this.category = category;
        this.description = description;
        this.image = image;
        this.featured = featured;
        this.estado = estado;
        this.price = price;
        this.stockTotal = stockTotal;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Stock> getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(List<Stock> stockTotal) {
        this.stockTotal = stockTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Clase embebida `Stock`
    public static class Stock {
        private String size;
        private String stock;

        public Stock(String size, String stock) {
            this.size = size;
            this.stock = stock;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }
    }
}


//        private String nombre;
//        private String categoria;
//        private String descripcion;
//        private String foto;
//        private boolean destacado;
//        private String estado;
//        private Double precio;
//        private int stock;


//        public ProductoDTO(String nombre, String categoria, String descripcion, String foto, boolean destacado, String estado, Double precio, int stock) {
//            this.nombre = nombre;
//            this.categoria = categoria;
//            this.descripcion = descripcion;
//            this.foto = foto;
//            this.destacado = destacado;
//            this.estado = estado;
//            this.precio = precio;
//            this.stock = stock;
//        }
//
//
//        public String getNombre() {
//            return nombre;
//        }
//
//        public void setNombre(String nombre) {
//            this.nombre = nombre;
//        }
//
//        public String getCategoria() {
//            return categoria;
//        }
//
//        public void setCategoria(String categoria) {
//            this.categoria = categoria;
//        }
//
//        public String getDescripcion() {
//            return descripcion;
//        }
//
//        public void setDescripcion(String descripcion) {
//            this.descripcion = descripcion;
//        }
//
//        public String getFoto() {
//            return foto;
//        }
//
//        public void setFoto(String foto) {
//            this.foto = foto;
//        }
//
//        public boolean isDestacado() {
//            return destacado;
//        }
//
//        public void setDestacado(boolean destacado) {
//            this.destacado = destacado;
//        }
//
//        public String getEstado() {
//            return estado;
//        }
//
//        public void setEstado(String estado) {
//            this.estado = estado;
//        }
//
//        public Double getPrecio() {
//            return precio;
//        }
//
//        public void setPrecio(Double precio) {
//            this.precio = precio;
//        }
//
//        public int getStock() {
//            return stock;
//        }
//
//        public void setStock(int stock) {
//            this.stock = stock;
//        }
//    }

