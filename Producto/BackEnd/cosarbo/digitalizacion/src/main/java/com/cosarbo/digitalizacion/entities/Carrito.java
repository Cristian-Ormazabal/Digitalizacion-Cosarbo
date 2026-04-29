package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;
    
    private Integer total; 
    private String estadoPedido;

    // Relación con el Usuario
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Carrito tiene muchos Items
    @OneToMany(mappedBy = "carrito")
    private List<itemCarrito> items;

    // Metodos indicados en el diagrama
    public void agregarItem() {}
    
    public void eliminarItem() {}
    
    public void calcularValor() {}
}