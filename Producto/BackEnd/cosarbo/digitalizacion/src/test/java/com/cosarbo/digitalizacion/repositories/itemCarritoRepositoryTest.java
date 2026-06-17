package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class itemCarritoRepositoryTest {

    @Autowired
    private itemCarritoRepository itemRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;


    @Test
    void debeBuscarItemsPorCarrito() {

        Usuario usuario = usuarioRepository.save(
                new Usuario("Juan", "juan1@test.com", "123", "CLIENTE")
        );

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito = carritoRepository.save(carrito);

        Producto producto = new Producto();
        producto.setNombre("Polera");
        producto.setPrecio(10000.0);
        producto.setStock(10);
        producto = productoRepository.save(producto);

        itemCarrito item = new itemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(2);
        item.setSubTotal(20000.0);

        itemRepository.save(item);

        List<itemCarrito> resultado =
                itemRepository.findByCarrito(carrito);

        assertEquals(1, resultado.size());
    }


    @Test
    void debeBuscarPorCarritoYProducto() {

        Usuario usuario = usuarioRepository.save(
                new Usuario("Pedro", "pedro@test.com", "123", "CLIENTE")
        );

        Carrito carrito = carritoRepository.save(new Carrito());
        carrito.setUsuario(usuario);
        carrito = carritoRepository.save(carrito);

        Producto producto = new Producto();
        producto.setNombre("Pantalon");
        producto.setPrecio(20000.0);
        producto.setStock(5);
        producto = productoRepository.save(producto);

        itemCarrito item = new itemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(1);
        item.setSubTotal(20000.0);

        itemRepository.save(item);

        Optional<itemCarrito> resultado = 
                itemRepository.findByCarritoAndProducto(carrito, producto);

        assertTrue(resultado.isPresent());
    }
}