package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.repositories.ReservaRepository;
import com.cosarbo.digitalizacion.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Retorna los días que ya tienen 5 o más bloques tomados
    @Override
    public List<LocalDate> obtenerDiasAgotados() {
        return reservaRepository.findDiasCompletamenteOcupados();
    }

    // Retorna la lista de horarios específicos
    @Override
    public List<String> obtenerHorasOcupadasPorDia(LocalDate fecha) {
        return reservaRepository.findHorasOcupadasPorFecha(fecha);
    }

    // Método para guardar la reserva final seleccionada por el cliente
    @Override
    public Reserva agendarReserva(Reserva reserva) {
        // Validación preventiva de seguridad por si intentan forzar un bloque duplicado desde Postman
        List<String> ocupadas = reservaRepository.findHorasOcupadasPorFecha(reserva.getFechaReserva());
        if (ocupadas.contains(reserva.getHoraReserva())) {
            throw new RuntimeException("Lo sentimos, este bloque horario ya fue agendado por otro cliente.");
        }
        return reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    @Override
    public List<Reserva> listarPorCorreo(String correo) {
        return reservaRepository.findByUsuarioCorreoOrderByFechaReservaDesc(correo);
    }
}