package com.cosarbo.digitalizacion.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TicketContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTicket;
    
    private String nombreUsuario; 
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}