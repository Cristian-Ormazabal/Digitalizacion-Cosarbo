package com.cosarbo.digitalizacion.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;
    
    private String nombre;
    private Integer precio;
    private Integer stock;

    // Metodo indicado en el diagrama
    public void actualizarStock() {
        // Aqui ira la lógica más adelante
    }
}