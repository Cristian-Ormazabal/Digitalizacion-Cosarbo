package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void constructorVacioDebeFuncionar() {

        Usuario usuario = new Usuario();

        assertNotNull(usuario);
    }

    @Test
    void constructorConParametrosDebeAsignarValores() {

        Usuario usuario = new Usuario(
                "Pedro",
                "pedro@test.com",
                "1234",
                "CLIENTE"
        );

        assertEquals("Pedro", usuario.getNombre());
        assertEquals("pedro@test.com", usuario.getCorreo());
        assertEquals("1234", usuario.getPassword());
        assertEquals("CLIENTE", usuario.getRol());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        Usuario usuario = new Usuario();

        List<Carrito> carritos = new ArrayList<>();

        usuario.setIdUsuario(1);
        usuario.setNombre("Juan");
        usuario.setCorreo("juan@test.com");
        usuario.setPassword("abcd");
        usuario.setRol("ADMIN");
        usuario.setCarritos(carritos);

        assertEquals(1, usuario.getIdUsuario());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("juan@test.com", usuario.getCorreo());
        assertEquals("abcd", usuario.getPassword());
        assertEquals("ADMIN", usuario.getRol());
        assertEquals(carritos, usuario.getCarritos());
    }
}