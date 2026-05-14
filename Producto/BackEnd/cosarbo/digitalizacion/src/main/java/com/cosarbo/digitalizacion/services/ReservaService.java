package com.cosarbo.digitalizacion.services;

import java.time.LocalDate;
import java.util.List;

import com.cosarbo.digitalizacion.entities.Reserva;

public interface ReservaService {
    List<LocalDate> obtenerFechasOcupadas();
    Reserva agendarCita(Reserva reserva);
    List<Reserva> listarTodas();
}