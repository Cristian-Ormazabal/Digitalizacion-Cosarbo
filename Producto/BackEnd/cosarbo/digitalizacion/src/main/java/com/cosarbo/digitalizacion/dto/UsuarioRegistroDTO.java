package com.cosarbo.digitalizacion.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    private String nombre;
    private String correo;
    private String password; 
    private String rol;
}