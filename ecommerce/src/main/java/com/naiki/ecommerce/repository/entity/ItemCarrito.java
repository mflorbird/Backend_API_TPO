package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
public class ItemCarrito {
    //get y set
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Carrito carrito;

    @ManyToOne
    private Producto producto;

    @Column
    private int cantidad;


    //Construcctor producto y cant

    public ItemCarrito() {
    } // constructor vacio para JPA

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;

    }
//    public void setId (Long id){
//        this.id = id;
//    }
//
//    public void setCarrito (Carrito carrito){
//        this.carrito = carrito;
//    }
//
//    public void setProducto (Producto producto){
//        this.producto = producto;
//    }
//
//    public void setCantidad ( int cantidad){
//        this.cantidad = cantidad;
//    }
//    public Long getId() {
//        return id;
//    }
//
//    public Carrito getCarrito() {
//        return carrito;
//    }
//
//    public Producto getProducto() {
//        return producto;
//    }
//
//    public int getCantidad() {
//        return cantidad;
//    }
//
//    public Long getProductoId() {
//        return producto.getId(); // Esto debería devolver un Long
//    }

}
