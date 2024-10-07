package com.naiki.ecommerce.dto;

public class ProductoDTO {

        private String nombre;
        private String categoria;
        private String descripcion;
        private String foto;
        private boolean destacado;
        private String estado;
        private Double precio;
        private int stock;


        public ProductoDTO(String nombre, String categoria, String descripcion, String foto, boolean destacado, String estado, Double precio, int stock) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.descripcion = descripcion;
            this.foto = foto;
            this.destacado = destacado;
            this.estado = estado;
            this.precio = precio;
            this.stock = stock;
        }


        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public boolean isDestacado() {
            return destacado;
        }

        public void setDestacado(boolean destacado) {
            this.destacado = destacado;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

