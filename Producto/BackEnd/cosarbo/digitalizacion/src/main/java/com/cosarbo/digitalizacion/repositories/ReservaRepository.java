package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    
    // Consulta para traer solo las fechas de las reservas futuras
    @Query("SELECT r.fechaReserva FROM Reserva r WHERE r.fechaReserva >= CURRENT_DATE")
    List<LocalDate> findFutureReservedDates();
}