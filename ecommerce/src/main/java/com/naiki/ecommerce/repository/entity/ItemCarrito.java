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

    @Column
    private String cartId;

    @Column
    private double price;

    @Column
    private String size;

    @Column
    private double subtotal;

    @Column
    private String img;


    //Construcctor producto y cant

    public ItemCarrito() {
    } // constructor vacio para JPA

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Constructor completo
    public ItemCarrito(Producto producto, int cantidad, String cartId, double price, String size, double subtotal, String img) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.cartId = cartId;
        this.price = price;
        this.size = size;
        this.subtotal = subtotal;
        this.img = img;
    }

    // para actualizar el subtotal basado en precio y cantidad
    public void actualizarSubtotal() {
        this.subtotal = this.price * this.cantidad;
    }
}
