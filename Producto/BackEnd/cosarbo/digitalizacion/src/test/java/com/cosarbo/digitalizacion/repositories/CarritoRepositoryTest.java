package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarritoRepositoryTest {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Test
    void debeBuscarCarritosPorIdUsuario() {

        Usuario usuario =
                usuarioRepository.save(
                    new Usuario(
                        "Mario",
                        "mario@test.com",
                        "123",
                        "CLIENTE"
                    )
                );


        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setEstado("PENDIENTE");

        carritoRepository.save(carrito);


        List<Carrito> resultado =
                carritoRepository.findByUsuario_IdUsuario(
                        usuario.getIdUsuario()
                );


        assertEquals(1, resultado.size());
    }


    @Test
    void debeBuscarCarritoPorUsuarioYEstado() {

        Usuario usuario =
                usuarioRepository.save(
                    new Usuario(
                        "Luis",
                        "luis@test.com",
                        "123",
                        "CLIENTE"
                    )
                );


        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setEstado("PAGADO");

        carritoRepository.save(carrito);


        Optional<Carrito> resultado =
                carritoRepository.findByUsuarioAndEstado(
                        usuario,
                        "PAGADO"
                );


        assertTrue(resultado.isPresent());
    }


    @Test
    void debeBuscarCarritosPorEstado() {

        Carrito carrito = new Carrito();
        carrito.setEstado("PAGADO");

        carritoRepository.save(carrito);


        List<Carrito> resultado =
                carritoRepository
                    .findByEstadoOrderByEstadoDesc("PAGADO");


        assertFalse(resultado.isEmpty());
    }
}