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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    
    private String nombre;
    private String correo;
    private String password;
    private String rol;

    // (1 Usuario tiene muchos Tickets)
    @OneToMany(mappedBy = "usuario")
    private List<TicketContacto> tickets;

    //(1 Usuario tiene 1 Carrito, según la línea)
    @OneToOne(mappedBy = "usuario")
    private Carrito carrito;
}