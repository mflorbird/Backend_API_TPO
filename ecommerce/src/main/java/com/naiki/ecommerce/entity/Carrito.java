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

    @OneToMany(mappedBy="carrito", cascade=CascadeType.ALL, orphanRemoval=true)// relacion c/productos del carrito
    public List<ItemCarrito> items= new ArrayList<>(); //lista de productos en el carro
    private double totalPrecio=0.0; //este es el precio del carro

    @ManyToOne // relacion con la entidad usuaro
    @JoinColumn(name="user_id")
    private Usuario usuario; // relaciona el carrito con el usuario

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

    public Usuario getUsuario(){
        return usuario;
    }
    public void setUsuario(Usuario usuario){
        this.usuario=usuario;
    }
}

//metodos

    public void agregarProducto(ItemCarrito item){
        this.items.add(item);
        recalcularTotal();
    }

    public void eliminarProducto(ItemCarrito item){
        this.items.remove(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.totalPrecio = items.stream() //stm para aplicar operaciones
                //el lambda toma el item y para cada item calcula el valor.
                .mapToDouble(item->item.getProducto().getPrecio() * item.getCantidad())
                .sum();
    }

}