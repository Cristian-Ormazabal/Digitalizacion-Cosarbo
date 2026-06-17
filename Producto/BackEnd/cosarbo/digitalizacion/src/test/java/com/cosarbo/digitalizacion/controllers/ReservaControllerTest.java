package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.repositories.ReservaRepository;
import com.cosarbo.digitalizacion.services.ReservaService;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import com.cosarbo.digitalizacion.services.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ServicioCosturaService servicioService;

    @InjectMocks
    private ReservaController controller;


    @Test
    void deberiaObtenerFechasOcupadas() {

        List<LocalDate> fechas = new ArrayList<>();

        when(reservaService.obtenerDiasAgotados())
                .thenReturn(fechas);

        var respuesta = controller.obtenerFechasOcupadas();

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(fechas, respuesta.getBody());
    }


    @Test
    void deberiaObtenerHorasOcupadas() {

        List<String> horas = List.of("10:00", "11:00");

        when(reservaService.obtenerHorasOcupadasPorDia(
                LocalDate.of(2025, 6, 20)))
                .thenReturn(horas);

        var respuesta = controller.obtenerHorasOcupadas("2025-06-20");

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(horas, respuesta.getBody());
    }


    @Test
    void deberiaAgendarReservaCorrectamente() {

        Usuario usuario = new Usuario();
        ServicioCostura servicio = new ServicioCostura();
        Reserva reserva = new Reserva();

        Map<String, Object> datos = Map.of(
                "idUsuario", 1,
                "idServicio", 2,
                "fechaReserva", "2025-06-20",
                "horaReserva", "10:00"
        );

        when(usuarioService.obtenerPorId(1))
                .thenReturn(usuario);

        when(servicioService.obtenerPorId(2))
                .thenReturn(servicio);

        when(reservaService.agendarReserva(any(Reserva.class)))
                .thenReturn(reserva);

        var respuesta = controller.agendarCita(datos);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(reserva, respuesta.getBody());
    }


    @Test
    void deberiaRetornarErrorSiNoHayHorario() {

        Map<String, Object> datos = Map.of(
                "idUsuario", 1,
                "idServicio", 2,
                "fechaReserva", "2025-06-20",
                "horaReserva", ""
        );

        var respuesta = controller.agendarCita(datos);

        assertEquals(400, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaRetornarErrorSiUsuarioNoExiste() {

        Map<String, Object> datos = Map.of(
                "idUsuario", 1,
                "idServicio", 2,
                "fechaReserva", "2025-06-20",
                "horaReserva", "10:00"
        );

        when(usuarioService.obtenerPorId(1))
                .thenReturn(null);

        var respuesta = controller.agendarCita(datos);

        assertEquals(400, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaListarTodasLasReservas() {

        List<Reserva> reservas = new ArrayList<>();

        when(reservaService.listarTodas())
                .thenReturn(reservas);

        var respuesta = controller.listarTodas();

        assertEquals(reservas, respuesta);
    }


    @Test
    void deberiaListarMisReservas() {

        Principal principal = mock(Principal.class);

        when(principal.getName())
                .thenReturn("cliente@test.com");

        List<Reserva> reservas = new ArrayList<>();

        when(reservaRepository
                .findByUsuarioCorreoOrderByFechaReservaDesc("cliente@test.com"))
                .thenReturn(reservas);

        var respuesta = controller.listarMisReservas(principal);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(reservas, respuesta.getBody());
    }
}