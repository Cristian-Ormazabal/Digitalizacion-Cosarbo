package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl service;

}

@Test
void debeListarTodosLosProductos() {

    Producto p1 = new Producto();
    Producto p2 = new Producto();

    when(productoRepository.findAll())
            .thenReturn(List.of(p1, p2));

    List<Producto> resultado = service.listarTodos();

    assertEquals(2, resultado.size());

    verify(productoRepository).findAll();
}

@Test
void debeObtenerProductoPorId() {

    Producto producto = new Producto();

    when(productoRepository.findById(1))
            .thenReturn(Optional.of(producto));

    Producto resultado = service.obtenerPorId(1);

    assertNotNull(resultado);

    verify(productoRepository).findById(1);
}

@Test
void debeLanzarErrorSiProductoNoExiste() {

    when(productoRepository.findById(1))
            .thenReturn(Optional.empty());

    RuntimeException error = assertThrows(
            RuntimeException.class,
            () -> service.obtenerPorId(1)
    );

    assertEquals(
            "Producto no encontrado con ID: 1",
            error.getMessage()
    );
}

@Test
void debeGuardarProducto() {

    Producto producto = new Producto();

    when(productoRepository.save(producto))
            .thenReturn(producto);

    Producto resultado = service.guardar(producto);

    assertNotNull(resultado);

    verify(productoRepository).save(producto);
}

@Test
void debeEliminarProductoPorId() {

    service.eliminar(1);

    verify(productoRepository).deleteById(1);
}

@Test
void debeBuscarProductoPorNombre() {

    Producto producto = new Producto();

    when(productoRepository
            .findByNombreContainingIgnoreCase("camisa"))
            .thenReturn(List.of(producto));

    List<Producto> resultado =
            service.buscarPorNombre("camisa");

    assertEquals(1, resultado.size());

    verify(productoRepository)
            .findByNombreContainingIgnoreCase("camisa");
}