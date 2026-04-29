package com.cosarbo.digitalizacion.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class itemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idItem;
    
    private Integer cantidad;
    private Integer subTotal;

    // Relación al carrito al que pertenece
    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = true)
    private Producto producto;


    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = true)
    private ServicioCostura servicioCostura;
}