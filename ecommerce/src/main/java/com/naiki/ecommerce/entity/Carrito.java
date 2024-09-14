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

}


