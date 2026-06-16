package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.*;
import com.cosarbo.digitalizacion.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

}

@Test
void debeRetornarCarritoPendienteExistente() {

    Integer usuarioId = 1;

    Carrito carrito = new Carrito();
    carrito.setEstado("PENDIENTE");

    when(carritoRepository.findByUsuario_IdUsuario(usuarioId))
            .thenReturn(List.of(carrito));

    Carrito resultado = carritoService.obtenerOCrearCarritoActivo(usuarioId);

    assertNotNull(resultado);
    assertEquals("PENDIENTE", resultado.getEstado());

    verify(carritoRepository, never()).save(any());
}

@Test
void debeCrearNuevoCarritoSiNoExistePendiente() {

    Integer usuarioId = 1;

    Usuario usuario = new Usuario();

    Carrito nuevoCarrito = new Carrito();
    nuevoCarrito.setEstado("PENDIENTE");
    nuevoCarrito.setUsuario(usuario);

    when(carritoRepository.findByUsuario_IdUsuario(usuarioId))
            .thenReturn(new ArrayList<>());

    when(usuarioRepository.findById(usuarioId))
            .thenReturn(Optional.of(usuario));

    when(carritoRepository.save(any(Carrito.class)))
            .thenReturn(nuevoCarrito);

    Carrito resultado = carritoService.obtenerOCrearCarritoActivo(usuarioId);

    assertNotNull(resultado);
    assertEquals("PENDIENTE", resultado.getEstado());

    verify(usuarioRepository).findById(usuarioId);
    verify(carritoRepository).save(any(Carrito.class));
}

@Test
void debeLanzarErrorSiUsuarioNoExiste() {

    Integer usuarioId = 1;

    when(carritoRepository.findByUsuario_IdUsuario(usuarioId))
            .thenReturn(List.of());

    when(usuarioRepository.findById(usuarioId))
            .thenReturn(Optional.empty());

    RuntimeException error = assertThrows(
        RuntimeException.class,
        () -> carritoService.obtenerOCrearCarritoActivo(usuarioId)
    );

    assertEquals("Usuario no encontrado", error.getMessage());
}

@Test
void debeFinalizarCompraCorrectamente() {

    Producto producto = new Producto();
    producto.setStock(10);
    producto.setNombre("Camisa");

    itemCarrito item = new itemCarrito();
    item.setProducto(producto);
    item.setCantidad(2);

    Carrito carrito = new Carrito();
    carrito.setEstado("PENDIENTE");
    carrito.setItems(List.of(item));

    when(carritoRepository.findById(1))
            .thenReturn(Optional.of(carrito));

    carritoService.finalizarCompra(1);

    assertEquals(8, producto.getStock());
    assertEquals("COMPLETADO", carrito.getEstado());

    verify(productoRepository).save(producto);
    verify(carritoRepository).save(carrito);
}

@Test
void debeLanzarErrorCuandoNoHayStock() {

    Producto producto = new Producto();
    producto.setStock(1);
    producto.setNombre("Camisa");

    itemCarrito item = new itemCarrito();
    item.setProducto(producto);
    item.setCantidad(5);

    Carrito carrito = new Carrito();
    carrito.setEstado("PENDIENTE");
    carrito.setItems(List.of(item));

    when(carritoRepository.findById(1))
            .thenReturn(Optional.of(carrito));

    RuntimeException error = assertThrows(
        RuntimeException.class,
        () -> carritoService.finalizarCompra(1)
    );

    assertEquals(
        "Stock insuficiente para: Camisa",
        error.getMessage()
    );

    verify(productoRepository, never()).save(any());
}

@Test
void debeRetornarCarritoPorId() {

    Carrito carrito = new Carrito();

    when(carritoRepository.findById(1))
            .thenReturn(Optional.of(carrito));

    Carrito resultado = carritoService.obtenerPorId(1);

    assertNotNull(resultado);
}