package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    // Relación con el usuario que agenda
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Relación con el servicio que se va a realizar
    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioCostura servicio;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(length = 20)
    private String estado = "Confirmada"; // Confirmada, Pendiente, Cancelada, Completada
}