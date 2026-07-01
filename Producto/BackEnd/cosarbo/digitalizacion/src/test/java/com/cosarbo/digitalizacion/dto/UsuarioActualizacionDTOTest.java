package com.cosarbo.digitalizacion.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioActualizacionDTOTest {

    @Test
    void gettersYSettersFuncionan() {

        UsuarioActualizacionDTO dto =
                new UsuarioActualizacionDTO();

        dto.setNombre("Juan");
        dto.setCorreo("juan@test.com");
        dto.setPassword("1234");

        assertEquals("Juan", dto.getNombre());
        assertEquals("juan@test.com", dto.getCorreo());
        assertEquals("1234", dto.getPassword());
    }
}