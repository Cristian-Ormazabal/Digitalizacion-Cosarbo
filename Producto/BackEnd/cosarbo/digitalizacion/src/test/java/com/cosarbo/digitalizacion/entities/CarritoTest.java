package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {

    @Test
    void constructorVacioDebeFuncionar() {

        Carrito carrito = new Carrito();

        assertNotNull(carrito);
        assertEquals("PENDIENTE", carrito.getEstado());
        assertNotNull(carrito.getItems());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        Carrito carrito = new Carrito();

        Usuario usuario = new Usuario();

        List<itemCarrito> items = new ArrayList<>();

        carrito.setIdCarrito(1);
        carrito.setItems(items);
        carrito.setEstado("PAGADO");
        carrito.setUsuario(usuario);

        assertEquals(1, carrito.getIdCarrito());
        assertEquals(items, carrito.getItems());
        assertEquals("PAGADO", carrito.getEstado());
        assertEquals(usuario, carrito.getUsuario());
    }
}