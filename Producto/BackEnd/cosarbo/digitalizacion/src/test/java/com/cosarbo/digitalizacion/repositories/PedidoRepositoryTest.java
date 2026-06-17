package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Test
    void debeBuscarTodosOrdenadosPorFechaVentaDesc() {

        Usuario usuario = usuarioRepository.save(
            new Usuario("Ana", "anaPedido@test.com", "123", "CLIENTE")
        );

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito = carritoRepository.save(carrito);

        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);
        pedido.setNombreReceptor("Ana");
        pedido.setDireccion("Calle 123");
        pedido.setComuna("Santiago");
        pedido.setCorreoContacto("ana@test.com");
        pedido.setTotalPagado(15000.0);

        pedidoRepository.save(pedido);

        List<Pedido> resultado =
                pedidoRepository.findAllByOrderByFechaVentaDesc();

        assertFalse(resultado.isEmpty());
    }


    @Test
    void debeBuscarPedidosPorCorreoUsuario() {

        Usuario usuario = usuarioRepository.save(
            new Usuario("Luis", "luisPedido@test.com", "123", "CLIENTE")
        );

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito = carritoRepository.save(carrito);

        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);
        pedido.setNombreReceptor("Luis");
        pedido.setDireccion("Calle 456");
        pedido.setComuna("Maipu");
        pedido.setCorreoContacto("luis@test.com");
        pedido.setTotalPagado(20000.0);

        pedidoRepository.save(pedido);

        List<Pedido> resultado =
            pedidoRepository.findByCarritoUsuarioCorreoOrderByFechaVentaDesc(
                "luisPedido@test.com"
            );

        assertEquals(1, resultado.size());
    }
}