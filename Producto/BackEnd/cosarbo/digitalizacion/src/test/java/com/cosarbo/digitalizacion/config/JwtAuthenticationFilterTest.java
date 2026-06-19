package com.cosarbo.digitalizacion.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeContinuarSiNoExisteAuthorizationHeader() throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        ReflectionTestUtils.invokeMethod(
                filter,
                "doFilterInternal",
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void debeContinuarSiHeaderNoEsBearer() throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic abc");

        ReflectionTestUtils.invokeMethod(
                filter,
                "doFilterInternal",
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void debeAutenticarConRol() throws Exception {

        String token = "jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenReturn("correo@test.com");

        when(jwtService.extractClaim(
                eq(token),
                any()))
                .thenReturn("ADMIN");

        when(jwtService.isTokenValid(
                token,
                "correo@test.com"))
                .thenReturn(true);

        ReflectionTestUtils.invokeMethod(
                filter,
                "doFilterInternal",
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void debeContinuarSiTokenNoEsValido() throws Exception {

        String token = "jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenReturn("correo@test.com");

        when(jwtService.isTokenValid(
                token,
                "correo@test.com"))
                .thenReturn(false);

        ReflectionTestUtils.invokeMethod(
                filter,
                "doFilterInternal",
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void debeContinuarSiRolEsNulo() throws Exception {

        String token = "jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenReturn("correo@test.com");

        when(jwtService.extractClaim(
                eq(token),
                any()))
                .thenReturn(null);

        when(jwtService.isTokenValid(
                token,
                "correo@test.com"))
                .thenReturn(true);

        ReflectionTestUtils.invokeMethod(
                filter,
                "doFilterInternal",
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }
}