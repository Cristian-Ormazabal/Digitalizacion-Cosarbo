package com.cosarbo.digitalizacion.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.repositories.ReservaRepository;
import com.cosarbo.digitalizacion.services.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository repository;

    @Override
    public List<LocalDate> obtenerFechasOcupadas() {
        return repository.findFutureReservedDates();
    }

    @Override
    public Reserva agendarCita(Reserva reserva) {
        return repository.save(reserva);
    }

    @Override
    public List<Reserva> listarTodas() {
        // Trae todas las reservas ordenadas por fecha para que el Admin las vea bien
        return repository.findAll();
    }
}