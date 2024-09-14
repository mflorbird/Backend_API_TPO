package com.naiki.ecommerce.repository.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.list;

@Entity // tabla en la BD.
public class Carrito{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // PARA GENERAR SOLO EL ID
    private long id;

    @OneToMany
    public List<ItemCarrito> items= new ArrayList<>(); //lista de productos en el carro
    private double totalPrecio=0.0; //este es el precio del carro

    // get y set

    public long getId() {
        return id;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public double getTotalPrecio() {
        return totalPrecio;
    }

    public void setTotalPrecio(double totalPrecio) {
        this.totalPrecio = totalPrecio;
    }
}


