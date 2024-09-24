package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
public class ItemCarrito {

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
}
