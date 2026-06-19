package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    void constructorVacioDebeFuncionar() {

        Producto producto = new Producto();

        assertNotNull(producto);
    }

    @Test
    void constructorConParametrosDebeAsignarValores() {

        Producto producto = new Producto(
                1,
                "Oso",
                15000.0,
                10,
                "Amigurumi tejido",
                "imagen.jpg"
        );

        assertEquals(1, producto.getIdProducto());
        assertEquals("Oso", producto.getNombre());
        assertEquals(15000.0, producto.getPrecio());
        assertEquals(10, producto.getStock());
        assertEquals("Amigurumi tejido", producto.getDescripcion());
        assertEquals("imagen.jpg", producto.getImagen());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        Producto producto = new Producto();

        List<itemCarrito> items = new ArrayList<>();

        producto.setIdProducto(5);
        producto.setNombre("Conejo");
        producto.setDescripcion("Descripción");
        producto.setPrecio(12000.0);
        producto.setStock(7);
        producto.setImagen("foto.png");
        producto.setItems(items);

        assertEquals(5, producto.getIdProducto());
        assertEquals("Conejo", producto.getNombre());
        assertEquals("Descripción", producto.getDescripcion());
        assertEquals(12000.0, producto.getPrecio());
        assertEquals(7, producto.getStock());
        assertEquals("foto.png", producto.getImagen());
        assertEquals(items, producto.getItems());
    }
}