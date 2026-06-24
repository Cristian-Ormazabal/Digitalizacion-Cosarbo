package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void debeSetearYObtenerCampos() {

        Pedido pedido = new Pedido();

        pedido.setIdPedido(1);
        pedido.setNombreReceptor("Juan");
        pedido.setCorreoContacto("juan@test.com");
        pedido.setDireccion("Calle 123");
        pedido.setComuna("Santiago");
        pedido.setTotalPagado(1000.0);

        assertEquals(1, pedido.getIdPedido());
        assertEquals("Juan", pedido.getNombreReceptor());
        assertEquals("juan@test.com", pedido.getCorreoContacto());
        assertEquals("Calle 123", pedido.getDireccion());
        assertEquals("Santiago", pedido.getComuna());
        assertEquals(1000.0, pedido.getTotalPagado());
    }

    @Test
    void constructorVacioDebeGenerarFecha() {

        Pedido pedido = new Pedido();

        assertNotNull(pedido.getFechaVenta());
    }
}