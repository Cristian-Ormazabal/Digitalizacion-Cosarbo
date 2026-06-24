package com.cosarbo.digitalizacion.services;

import java.time.LocalDate;
import java.util.List;

import com.cosarbo.digitalizacion.entities.Reserva;

public interface ReservaService {
    List<LocalDate> obtenerDiasAgotados();
    Reserva agendarReserva(Reserva reserva);
    List<String> obtenerHorasOcupadasPorDia(LocalDate fecha);
    List<Reserva> listarTodas();
    List<Reserva> listarPorCorreo(String correo);
}