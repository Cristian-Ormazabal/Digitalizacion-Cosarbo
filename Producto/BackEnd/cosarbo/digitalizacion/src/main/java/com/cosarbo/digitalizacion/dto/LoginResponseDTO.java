package com.cosarbo.digitalizacion.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String mensaje;
    private UsuarioDTO usuario;
    private String token; 
}