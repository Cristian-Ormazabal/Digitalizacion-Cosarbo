package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.config.JwtService;
import com.cosarbo.digitalizacion.dto.UsuarioActualizacionDTO;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.services.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioController controller;


    @Test
    void deberiaListarUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        when(usuarioService.listarTodos())
                .thenReturn(usuarios);

        var respuesta = controller.listar();

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(usuarios, respuesta.getBody());
    }


    @Test
    void deberiaObtenerUsuarioPorId() {

        Usuario usuario = new Usuario();

        when(usuarioService.obtenerPorId(1))
                .thenReturn(usuario);

        var respuesta = controller.obtener(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(usuario, respuesta.getBody());
    }


    @Test
    void deberiaRetornar404SiUsuarioNoExiste() {

        when(usuarioService.obtenerPorId(99))
                .thenReturn(null);

        var respuesta = controller.obtener(99);

        assertEquals(404, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaCrearUsuario() {

        Usuario usuario = new Usuario();

        when(usuarioService.guardar(usuario))
                .thenReturn(usuario);

        var respuesta = controller.crear(usuario);

        assertEquals(201, respuesta.getStatusCode().value());
        assertEquals(usuario, respuesta.getBody());
    }


    @Test
    void deberiaIniciarSesionCorrectamente() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(1);
        dto.setNombre("Pedro");
        dto.setCorreo("pedro@test.com");
        dto.setRol("CLIENTE");

        when(usuarioService.login("pedro@test.com", "1234"))
                .thenReturn(dto);

        when(jwtService.generateToken("pedro@test.com", "CLIENTE"))
                .thenReturn("token123");

        Map<String, String> datos = Map.of(
                "correo", "pedro@test.com",
                "password", "1234"
        );

        var respuesta = controller.login(datos);

        assertEquals(200, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaRechazarLoginIncorrecto() {

        when(usuarioService.login("falso@test.com", "xxx"))
                .thenReturn(null);

        Map<String, String> datos = Map.of(
                "correo", "falso@test.com",
                "password", "xxx"
        );

        var respuesta = controller.login(datos);

        assertEquals(401, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaFinalizarCompraCorrectamente() {

        UsuarioDTO dto = new UsuarioDTO();

        Map<String, Object> datos = Map.of(
                "direccion", "Calle 123"
        );

        when(usuarioService.finalizarCompra(1, datos))
                .thenReturn(dto);

        var respuesta = controller.finalizarCompra(1, datos);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(dto, respuesta.getBody());
    }


    @Test
    void deberiaRegistrarUsuarioCorrectamente() {

        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioService.registrarNuevoUsuario(usuario))
                .thenReturn(dto);

        var respuesta = controller.registrarUsuario(usuario);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(dto, respuesta.getBody());
    }


    @Test
    void deberiaRetornarErrorSiRegistroFalla() {

        Usuario usuario = new Usuario();

        when(usuarioService.registrarNuevoUsuario(usuario))
                .thenThrow(new RuntimeException("Correo existente"));

        var respuesta = controller.registrarUsuario(usuario);

        assertEquals(400, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaActualizarPerfilCorrectamente() {

        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();

        var respuesta = controller.actualizarPerfil(1, dto);

        verify(usuarioService).actualizarUsuario(1, dto);

        assertEquals(200, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaEliminarUsuario() {

        var respuesta = controller.eliminar(1);

        verify(usuarioService).eliminar(1);

        assertEquals(204, respuesta.getStatusCode().value());
    }
}