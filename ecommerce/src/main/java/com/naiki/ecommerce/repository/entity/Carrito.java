package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // tabla en la BD.
@Data
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PARA GENERAR SOLO EL ID
    private long id;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)// relacion c/productos del carrito
    public List<ItemCarrito> items = new ArrayList<>(); //lista de productos en el carro

    @Column
    private double totalPrecio = 0.0; //este es el precio del carro

    @ManyToOne // relacion con la entidad usuaro
    private User usuario; // relaciona el carrito con el usuario

    @Column
    private LocalDateTime fechaCreacion; // la fecha para que la agarre mostrar perfil

    @Column
    private String estado;

    @Column
    private String codigoDescuento;

    @Column
    private double porcentajeDescuentoAplicado = 0.0;

    @Column
    private double totalOriginal;

    // constr. para la fecha

    public Carrito(){
        this.fechaCreacion = LocalDateTime.now();
    }


//metodos

    public void agregarProducto(ItemCarrito item) {
        this.items.add(item);
        recalcularTotal();
    }

    public void eliminarProducto(ItemCarrito item) {
        this.items.remove(item);
        recalcularTotal();
    }

    public void vaciarCarrito() {
        this.items.clear();
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.totalOriginal = items.stream() //stm para aplicar operaciones
                //el lambda toma el item y para cada item calcula el valor.
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
        this.totalPrecio = totalOriginal;

        //agrego el descuento si tiene
        if (porcentajeDescuentoAplicado>0){
            this.totalPrecio=this.totalPrecio - (totalOriginal * porcentajeDescuentoAplicado);
        }
    }

    //metodo para codigo descuento
    public void aplicarDescuento(String codigoDescuento, double porcentajeDescuento){
        this.codigoDescuento=codigoDescuento;
        this.porcentajeDescuentoAplicado=porcentajeDescuento;
        recalcularTotal();
    }

    public Object getFechaTransaccion() {
        return fechaCreacion;
    }
}
