package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class itemCarritoTest {

    @Test
    void constructorVacioDebeFuncionar() {

        itemCarrito item = new itemCarrito();

        assertNotNull(item);
    }

    @Test
    void constructorConParametrosDebeAsignarValores() {

        Carrito carrito = new Carrito();
        Producto producto = new Producto();

        itemCarrito item = new itemCarrito(
                carrito,
                producto,
                3,
                1500.0
        );

        assertEquals(carrito, item.getCarrito());
        assertEquals(producto, item.getProducto());
        assertEquals(3, item.getCantidad());
        assertEquals(1500.0, item.getSubTotal());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        itemCarrito item = new itemCarrito();

        Carrito carrito = new Carrito();
        Producto producto = new Producto();

        item.setIdItem(10);
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(5);
        item.setSubTotal(2500.0);

        assertEquals(10, item.getIdItem());
        assertEquals(carrito, item.getCarrito());
        assertEquals(producto, item.getProducto());
        assertEquals(5, item.getCantidad());
        assertEquals(2500.0, item.getSubTotal());
    }

    @Test
    void getSubtotalDebeRetornarSubTotal() {

        itemCarrito item = new itemCarrito();

        item.setSubTotal(999.0);

        assertEquals(999.0, item.getSubtotal());
    }
}