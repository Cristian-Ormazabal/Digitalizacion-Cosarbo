package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Pedido;
import com.cosarbo.digitalizacion.repositories.PedidoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoController controller;


    @Test
    void deberiaListarTodosLosPedidos() {

        List<Pedido> pedidos = new ArrayList<>();

        when(pedidoRepository.findAllByOrderByFechaVentaDesc())
                .thenReturn(pedidos);

        List<Pedido> respuesta = controller.listarTodosLosPedidos();

        assertEquals(pedidos, respuesta);
    }


    @Test
    void deberiaObtenerPedidoPorId() {

        Pedido pedido = new Pedido();

        when(pedidoRepository.findById(1))
                .thenReturn(Optional.of(pedido));

        var respuesta = controller.obtenerPedidoPorId(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(pedido, respuesta.getBody());
    }


    @Test
    void deberiaRetornar404SiPedidoNoExiste() {

        when(pedidoRepository.findById(99))
                .thenReturn(Optional.empty());

        var respuesta = controller.obtenerPedidoPorId(99);

        assertEquals(404, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaListarMisPedidos() {

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("cliente@test.com");

        List<Pedido> pedidos = new ArrayList<>();

        when(pedidoRepository
                .findByCarritoUsuarioCorreoOrderByFechaVentaDesc("cliente@test.com"))
                .thenReturn(pedidos);

        var respuesta = controller.listarMisPedidos(principal);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(pedidos, respuesta.getBody());
    }


    @Test
    void deberiaEliminarPedidoExistente() {

        when(pedidoRepository.existsById(1))
                .thenReturn(true);

        var respuesta = controller.eliminarPedido(1);

        verify(pedidoRepository).deleteById(1);

        assertEquals(204, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaRetornar404AlEliminarPedidoInexistente() {

        when(pedidoRepository.existsById(100))
                .thenReturn(false);

        var respuesta = controller.eliminarPedido(100);

        verify(pedidoRepository, never()).deleteById(100);

        assertEquals(404, respuesta.getStatusCode().value());
    }
}