package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    
   // Trae las horas ocupadas de un día específico
    @Query("SELECT r.horaReserva FROM Reserva r WHERE r.fechaReserva = :fecha")
    List<String> findHorasOcupadasPorFecha(@Param("fecha") LocalDate fecha);

    // Trae los días futuros donde la cantidad de reservas sea igual o mayor a 5
    @Query("SELECT r.fechaReserva FROM Reserva r WHERE r.fechaReserva >= CURRENT_DATE GROUP BY r.fechaReserva HAVING COUNT(r) >= 5")
    List<LocalDate> findDiasCompletamenteOcupados();

    List<Reserva> findByUsuarioCorreoOrderByFechaReservaDesc(String correo);
}