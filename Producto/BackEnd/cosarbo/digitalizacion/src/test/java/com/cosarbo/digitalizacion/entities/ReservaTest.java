package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void constructorVacioDebeFuncionar() {

        Reserva reserva = new Reserva();

        assertNotNull(reserva);
    }

    @Test
    void constructorCompletoDebeAsignarValores() {

        Usuario usuario = new Usuario();
        ServicioCostura servicio = new ServicioCostura();
        LocalDate fecha = LocalDate.of(2025, 6, 20);

        Reserva reserva = new Reserva(
                1,
                usuario,
                servicio,
                fecha,
                "Confirmada",
                "10:00"
        );

        assertEquals(1, reserva.getIdReserva());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(servicio, reserva.getServicio());
        assertEquals(fecha, reserva.getFechaReserva());
        assertEquals("Confirmada", reserva.getEstado());
        assertEquals("10:00", reserva.getHoraReserva());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        Reserva reserva = new Reserva();

        Usuario usuario = new Usuario();
        ServicioCostura servicio = new ServicioCostura();
        LocalDate fecha = LocalDate.now();

        reserva.setIdReserva(5);
        reserva.setUsuario(usuario);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(fecha);
        reserva.setEstado("Pendiente");
        reserva.setHoraReserva("15:30");

        assertEquals(5, reserva.getIdReserva());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(servicio, reserva.getServicio());
        assertEquals(fecha, reserva.getFechaReserva());
        assertEquals("Pendiente", reserva.getEstado());
        assertEquals("15:30", reserva.getHoraReserva());
    }
}