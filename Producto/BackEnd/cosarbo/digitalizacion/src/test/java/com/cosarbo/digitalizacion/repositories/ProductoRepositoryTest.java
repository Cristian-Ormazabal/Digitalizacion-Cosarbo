package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void debeBuscarProductoPorNombre() {

        Producto producto = new Producto();
        producto.setNombre("Pantalon");
        producto.setPrecio(10000.0);
        producto.setStock(5);

        productoRepository.save(producto);

        List<Producto> resultado =
                productoRepository.findByNombreContainingIgnoreCase("pantal");

        assertFalse(resultado.isEmpty());
        assertEquals("Pantalon", resultado.get(0).getNombre());
    }

    @Test
    void debeBuscarProductosConStockMayorACero() {

        Producto p1 = new Producto();
        p1.setNombre("Polera");
        p1.setPrecio(5000.0);
        p1.setStock(10);

        Producto p2 = new Producto();
        p2.setNombre("Calcetin");
        p2.setPrecio(1000.0);
        p2.setStock(0);

        productoRepository.save(p1);
        productoRepository.save(p2);

        List<Producto> resultado =
                productoRepository.findByStockGreaterThan(0);

        assertEquals(1, resultado.size());
        assertEquals("Polera", resultado.get(0).getNombre());
    }
}