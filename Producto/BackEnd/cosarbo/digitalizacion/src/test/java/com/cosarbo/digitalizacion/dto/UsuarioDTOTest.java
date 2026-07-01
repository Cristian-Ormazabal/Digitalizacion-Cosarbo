package com.cosarbo.digitalizacion.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

    @Test
    void gettersYSettersFuncionan() {

        UsuarioDTO dto = new UsuarioDTO();

        dto.setIdUsuario(1);
        dto.setNombre("Pedro");
        dto.setCorreo("pedro@test.com");
        dto.setRol("CLIENTE");
        dto.setIdCarrito(10);

        assertEquals(1, dto.getIdUsuario());
        assertEquals("Pedro", dto.getNombre());
        assertEquals("pedro@test.com", dto.getCorreo());
        assertEquals("CLIENTE", dto.getRol());
        assertEquals(10, dto.getIdCarrito());
    }

    @Test
    void constructorCompletoFunciona() {

        UsuarioDTO dto =
                new UsuarioDTO(
                        1,
                        "Pedro",
                        "pedro@test.com",
                        "CLIENTE",
                        10
                );

        assertEquals(1, dto.getIdUsuario());
        assertEquals("Pedro", dto.getNombre());
        assertEquals("pedro@test.com", dto.getCorreo());
        assertEquals("CLIENTE", dto.getRol());
        assertEquals(10, dto.getIdCarrito());
    }
}