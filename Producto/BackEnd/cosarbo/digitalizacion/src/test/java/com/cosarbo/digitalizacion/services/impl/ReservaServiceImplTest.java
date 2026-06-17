package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.repositories.ReservaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl service;



        @Test
        void debeObtenerDiasAgotados() {

        List<LocalDate> fechas = List.of(
                LocalDate.of(2025, 6, 10),
                LocalDate.of(2025, 6, 15)
        );

        when(reservaRepository.findDiasCompletamenteOcupados())
                .thenReturn(fechas);

        List<LocalDate> resultado = service.obtenerDiasAgotados();

        assertEquals(2, resultado.size());

        verify(reservaRepository)
                .findDiasCompletamenteOcupados();
        }

        @Test
        void debeObtenerHorasOcupadasPorDia() {

        LocalDate fecha = LocalDate.of(2025, 6, 10);

        List<String> horas = List.of("10:00", "11:00");

        when(reservaRepository.findHorasOcupadasPorFecha(fecha))
                .thenReturn(horas);

        List<String> resultado =
                service.obtenerHorasOcupadasPorDia(fecha);

        assertEquals(2, resultado.size());

        verify(reservaRepository)
                .findHorasOcupadasPorFecha(fecha);
        }


        @Test
        void debeAgendarReservaCuandoHorarioEstaDisponible() {

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.of(2025, 7, 20));
        reserva.setHoraReserva("10:00");

        when(reservaRepository.findHorasOcupadasPorFecha(
                reserva.getFechaReserva()))
                .thenReturn(List.of("09:00", "11:00"));

        when(reservaRepository.save(reserva))
                .thenReturn(reserva);

        Reserva resultado = service.agendarReserva(reserva);

        assertNotNull(resultado);

        verify(reservaRepository)
                .save(reserva);
        }

        @Test
        void debeLanzarErrorSiHorarioYaEstaOcupado() {

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.of(2025, 7, 20));
        reserva.setHoraReserva("10:00");

        when(reservaRepository.findHorasOcupadasPorFecha(
                reserva.getFechaReserva()))
                .thenReturn(List.of("10:00", "11:00"));

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.agendarReserva(reserva)
        );

        assertEquals(
                "Lo sentimos, este bloque horario ya fue agendado por otro cliente.",
                error.getMessage()
        );

        verify(reservaRepository, never())
                .save(any(Reserva.class));
        }

        @Test
        void debeListarTodasLasReservas() {

        List<Reserva> reservas = List.of(
                new Reserva(),
                new Reserva()
        );

        when(reservaRepository.findAll())
                .thenReturn(reservas);

        List<Reserva> resultado = service.listarTodas();

        assertEquals(2, resultado.size());

        verify(reservaRepository)
                .findAll();
        }

        @Test
        void debeListarReservasPorCorreo() {

        String correo = "cliente@test.com";

        List<Reserva> reservas = List.of(new Reserva());

        when(reservaRepository
                .findByUsuarioCorreoOrderByFechaReservaDesc(correo))
                .thenReturn(reservas);

        List<Reserva> resultado =
                service.listarPorCorreo(correo);

        assertEquals(1, resultado.size());

        verify(reservaRepository)
                .findByUsuarioCorreoOrderByFechaReservaDesc(correo);
        }


}
