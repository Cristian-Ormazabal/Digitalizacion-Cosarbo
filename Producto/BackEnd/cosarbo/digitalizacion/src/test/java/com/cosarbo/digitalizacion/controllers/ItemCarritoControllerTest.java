package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.dto.itemCarritoDTO;
import com.cosarbo.digitalizacion.services.itemCarritoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ItemCarritoControllerTest {

    @Mock
    private itemCarritoService itemCarritoService;

    @InjectMocks
    private ItemCarritoController controller;


    @Test
    void deberiaAgregarProducto() {

        itemCarritoDTO dto = new itemCarritoDTO();
        itemCarrito item = new itemCarrito();

        when(itemCarritoService.agregarProducto(dto))
                .thenReturn(item);

        var respuesta = controller.agregarProducto(dto);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(item, respuesta.getBody());
    }


    @Test
    void deberiaListarItemsPorCarrito() {

        List<itemCarrito> lista = new ArrayList<>();

        when(itemCarritoService.listarPorCarrito(1))
                .thenReturn(lista);

        var respuesta = controller.listarPorCarrito(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(lista, respuesta.getBody());
    }


    @Test
    void deberiaEliminarItem() {

        var respuesta = controller.eliminarItem(1);

        verify(itemCarritoService).eliminar(1);

        assertEquals(204, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaActualizarCantidad() {

        itemCarrito item = new itemCarrito();

        when(itemCarritoService.actualizarCantidad(1, 5))
                .thenReturn(item);

        var respuesta = controller.actualizarCantidad(1, 5);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(item, respuesta.getBody());
    }
}