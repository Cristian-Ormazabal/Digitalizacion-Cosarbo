package com.cosarbo.digitalizacion.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                86400000L
        );
    }

    @Test
    void generateTokenDebeRetornarTokenValido() {

        String token =
                jwtService.generateToken(
                        "correo@test.com",
                        "CLIENTE"
                );

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsernameDebeRetornarCorreo() {

        String token =
                jwtService.generateToken(
                        "correo@test.com",
                        "CLIENTE"
                );

        String username =
                jwtService.extractUsername(token);

        assertEquals(
                "correo@test.com",
                username
        );
    }

    @Test
    void isTokenValidDebeRetornarTrue() {

        String token =
                jwtService.generateToken(
                        "correo@test.com",
                        "CLIENTE"
                );

        boolean valido =
                jwtService.isTokenValid(
                        token,
                        "correo@test.com"
                );

        assertTrue(valido);
    }

    @Test
    void isTokenValidDebeRetornarFalseCuandoCorreoNoCoincide() {

        String token =
                jwtService.generateToken(
                        "correo@test.com",
                        "CLIENTE"
                );

        boolean valido =
                jwtService.isTokenValid(
                        token,
                        "otro@test.com"
                );

        assertFalse(valido);
    }

    @Test
    void extractClaimDebeRetornarRol() {

        String token =
                jwtService.generateToken(
                        "correo@test.com",
                        "ADMIN"
                );

        String rol =
                jwtService.extractClaim(
                        token,
                        claims -> claims.get("rol", String.class)
                );

        assertEquals(
                "ADMIN",
                rol
        );
    }
}