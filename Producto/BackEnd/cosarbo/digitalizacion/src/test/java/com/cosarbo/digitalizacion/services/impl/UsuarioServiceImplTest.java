package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.dto.UsuarioActualizacionDTO;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.entities.*;
import com.cosarbo.digitalizacion.repositories.*;
import com.cosarbo.digitalizacion.services.PedidoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private itemCarritoRepository itemCarritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl service;

    @Test
    void debeListarUsuarios() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(new Usuario(), new Usuario()));

        List<Usuario> resultado = service.listarTodos();

        assertEquals(2, resultado.size());

        verify(usuarioRepository).findAll();
    }

    @Test
    void debeGuardarUsuarioEncriptandoPassword() {

        Usuario usuario = new Usuario();
        usuario.setPassword("123456");

        when(passwordEncoder.encode("123456"))
                .thenReturn("hash123");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado = service.guardar(usuario);

        assertEquals("hash123", resultado.getPassword());

        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void noDebeEncriptarPasswordYaEncriptada() {

        Usuario usuario = new Usuario();
        usuario.setPassword("$2a$abcdefghijklmn");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        service.guardar(usuario);

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void debeGuardarUsuarioConPasswordNull() {

        Usuario usuario = new Usuario();
        usuario.setPassword(null);

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        Usuario resultado = service.guardar(usuario);

        assertNotNull(resultado);

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void debeObtenerUsuarioPorId() {

        Usuario usuario = new Usuario();

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = service.obtenerPorId(1);

        assertNotNull(resultado);
    }

    @Test
    void debeEliminarUsuario() {

        service.eliminar(1);

        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void debeIniciarSesionCorrectamente() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre("Juan");
        usuario.setCorreo("juan@test.com");
        usuario.setRol("CLIENTE");
        usuario.setPassword("hash");

        Carrito carrito = new Carrito();
        carrito.setIdCarrito(10);

        when(usuarioRepository.findByCorreo("juan@test.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("1234", "hash"))
                .thenReturn(true);

        when(carritoRepository.findByUsuarioAndEstado(usuario, "PENDIENTE"))
                .thenReturn(Optional.of(carrito));

        UsuarioDTO dto = service.login("juan@test.com", "1234");

        assertNotNull(dto);
        assertEquals(1, dto.getIdUsuario());
        assertEquals(10, dto.getIdCarrito());
    }

    @Test
    void debeCrearCarritoSiNoExisteUnoPendiente() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre("Juan");
        usuario.setCorreo("juan@test.com");
        usuario.setRol("CLIENTE");
        usuario.setPassword("hash");

        Carrito carritoNuevo = new Carrito();
        carritoNuevo.setIdCarrito(99);

        when(usuarioRepository.findByCorreo("juan@test.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("1234", "hash"))
                .thenReturn(true);

        when(carritoRepository.findByUsuarioAndEstado(usuario, "PENDIENTE"))
                .thenReturn(Optional.empty());

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carritoNuevo);

        UsuarioDTO dto =
                service.login("juan@test.com", "1234");

        assertNotNull(dto);
        assertEquals(99, dto.getIdCarrito());

        verify(carritoRepository)
                .save(any(Carrito.class));
    }

    @Test
    void debeRetornarNullSiPasswordIncorrecta() {

        Usuario usuario = new Usuario();
        usuario.setPassword("hash");

        when(usuarioRepository.findByCorreo("correo"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("mala", "hash"))
                .thenReturn(false);

        UsuarioDTO resultado = service.login("correo", "mala");

        assertNull(resultado);
    }

    @Test
    void debeRetornarNullSiUsuarioNoExiste() {

        when(usuarioRepository.findByCorreo("noexiste@test.com"))
                .thenReturn(Optional.empty());

        UsuarioDTO resultado =
                service.login("noexiste@test.com", "1234");

        assertNull(resultado);
    }

    @Test
    void debeRegistrarNuevoUsuario() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setCorreo("ana@test.com");
        usuario.setPassword("1234");

        when(usuarioRepository.findByCorreo(usuario.getCorreo()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("hash");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(i -> i.getArgument(0));

        Carrito carrito = new Carrito();
        carrito.setIdCarrito(5);

        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(carrito);

        UsuarioDTO resultado = service.registrarNuevoUsuario(usuario);

        assertEquals("CLIENTE", usuario.getRol());
        assertEquals(5, resultado.getIdCarrito());
    }

    @Test
    void debeLanzarErrorSiCorreoYaExiste() {

        Usuario usuario = new Usuario();
        usuario.setCorreo("existente@test.com");

        when(usuarioRepository.findByCorreo(usuario.getCorreo()))
                .thenReturn(Optional.of(new Usuario()));

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.registrarNuevoUsuario(usuario)
        );

        assertEquals("El correo ya está registrado", error.getMessage());
    }

    @Test
    void debeActualizarUsuarioConNuevaPassword() {

        Usuario usuario = new Usuario();

        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();
        dto.setNombre("Pedro");
        dto.setCorreo("pedro@test.com");
        dto.setPassword("1234");

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.encode("1234"))
                .thenReturn("hash");

        service.actualizarUsuario(1, dto);

        assertEquals("Pedro", usuario.getNombre());
        assertEquals("pedro@test.com", usuario.getCorreo());
        assertEquals("hash", usuario.getPassword());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void debeActualizarUsuarioSinModificarPassword() {

        Usuario usuario = new Usuario();
        usuario.setPassword("hashViejo");

        UsuarioActualizacionDTO dto =
                new UsuarioActualizacionDTO();

        dto.setNombre("Pedro");
        dto.setCorreo("pedro@test.com");
        dto.setPassword("");

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        service.actualizarUsuario(1, dto);

        assertEquals("Pedro", usuario.getNombre());
        assertEquals("pedro@test.com", usuario.getCorreo());
        assertEquals("hashViejo", usuario.getPassword());

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExisteAlActualizar() {

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.empty());

        UsuarioActualizacionDTO dto =
                new UsuarioActualizacionDTO();

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.actualizarUsuario(1, dto));

        assertTrue(
                ex.getMessage().contains("Usuario no encontrado")
        );
    }
}