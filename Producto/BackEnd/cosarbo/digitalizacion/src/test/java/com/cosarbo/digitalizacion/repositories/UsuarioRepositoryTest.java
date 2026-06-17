package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Test
    void debeBuscarUsuarioPorCorreo() {

        Usuario usuario =
                new Usuario(
                        "Juan",
                        "juan@test.com",
                        "1234",
                        "CLIENTE"
                );

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado =
                usuarioRepository.findByCorreo("juan@test.com");

        assertTrue(resultado.isPresent());
    }


    @Test
    void debeBuscarPorCorreoYPassword() {

        Usuario usuario =
                new Usuario(
                        "Pedro",
                        "pedro@test.com",
                        "abcd",
                        "CLIENTE"
                );

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado =
                usuarioRepository.findByCorreoAndPassword(
                        "pedro@test.com",
                        "abcd"
                );

        assertTrue(resultado.isPresent());
    }


    @Test
    void debeVerificarExistenciaDeCorreo() {

        Usuario usuario =
                new Usuario(
                        "Ana",
                        "ana@test.com",
                        "123",
                        "ADMIN"
                );

        usuarioRepository.save(usuario);

        assertTrue(
                usuarioRepository.existsByCorreo("ana@test.com")
        );
    }
}