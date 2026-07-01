package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioCosturaRepository servicioRepository;


    @Test
    void debeBuscarHorasOcupadasPorFecha() {

        Usuario usuario = usuarioRepository.save(
            new Usuario("Mario", "marioReserva@test.com", "123", "CLIENTE")
        );

        ServicioCostura servicio = new ServicioCostura();
        servicio.setTipoPrenda("Pantalon");
        servicio.setCosto(5000);
        servicio = servicioRepository.save(servicio);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraReserva("10:00");

        reservaRepository.save(reserva);

        List<String> horas =
            reservaRepository.findHorasOcupadasPorFecha(LocalDate.now());

        assertTrue(horas.contains("10:00"));
    }


    @Test
    void debeBuscarReservasPorCorreoUsuario() {

        Usuario usuario = usuarioRepository.save(
            new Usuario("Pedro", "pedroReserva@test.com", "123", "CLIENTE")
        );

        ServicioCostura servicio = new ServicioCostura();
        servicio.setTipoPrenda("Chaqueta");
        servicio.setCosto(8000);
        servicio = servicioRepository.save(servicio);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraReserva("12:00");

        reservaRepository.save(reserva);

        List<Reserva> resultado =
            reservaRepository.findByUsuarioCorreoOrderByFechaReservaDesc(
                "pedroReserva@test.com"
            );

        assertEquals(1, resultado.size());
    }
}