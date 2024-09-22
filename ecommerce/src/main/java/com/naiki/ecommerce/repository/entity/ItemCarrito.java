package com.naiki.ecommerce.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;


@Data
@Entity
public class ItemCarrito{
    //get y set
    @Column
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="carrito_id")
    private Carrito carrito;

    @Column
    @ManyToOne
    @JoinColumn(name="producto_id")
    private Producto producto;

    @Column
    private int cantidad;


    //Construcctor producto y cant

//    public ItemCarrito(){} // constructor vacio para JPA
//
//    public ItemCarrito(Producto producto, int cantidad){
//        this.producto=producto;
//        this.cantidad=cantidad;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setCarrito(Carrito carrito) {
//        this.carrito = carrito;
//    }
//
//    public void setProducto(Producto producto) {
//        this.producto = producto;
//    }
//
//    public void setCantidad(int cantidad) {
//        this.cantidad = cantidad;
//    }

}