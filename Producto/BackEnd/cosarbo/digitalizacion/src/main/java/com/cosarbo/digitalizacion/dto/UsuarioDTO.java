package com.cosarbo.digitalizacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;

    private List<Integer> idsTickets; 
    private Integer idCarrito;
}