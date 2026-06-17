package com.cosarbo.digitalizacion.controllers;

import com.cloudinary.Cloudinary;
import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.ProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private ProductoController controller;


    @Test
    void deberiaListarProductos() {

        List<Producto> productos = new ArrayList<>();

        when(productoService.listarTodos())
                .thenReturn(productos);

        var respuesta = controller.listarProductos();

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(productos, respuesta.getBody());
    }


    @Test
    void deberiaObtenerProductoPorId() {

        Producto producto = new Producto();

        when(productoService.obtenerPorId(1))
                .thenReturn(producto);

        var respuesta = controller.obtenerProducto(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(producto, respuesta.getBody());
    }


    @Test
    void deberiaRetornar404SiNoExisteProducto() {

        when(productoService.obtenerPorId(99))
                .thenThrow(new RuntimeException());

        var respuesta = controller.obtenerProducto(99);

        assertEquals(404, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaBuscarProductosPorNombre() {

        List<Producto> productos = new ArrayList<>();

        when(productoService.buscarPorNombre("oso"))
                .thenReturn(productos);

        var respuesta = controller.buscar("oso");

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(productos, respuesta.getBody());
    }


    @Test
    void deberiaEliminarProductoExistente() {

        Producto producto = new Producto();

        when(productoRepository.findById(1))
                .thenReturn(Optional.of(producto));

        var respuesta = controller.eliminarProducto(1);

        verify(productoRepository).delete(producto);

        assertEquals(200, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaRetornar404AlEliminarProductoInexistente() {

        when(productoRepository.findById(100))
                .thenReturn(Optional.empty());

        var respuesta = controller.eliminarProducto(100);

        assertEquals(404, respuesta.getStatusCode().value());
    }
}