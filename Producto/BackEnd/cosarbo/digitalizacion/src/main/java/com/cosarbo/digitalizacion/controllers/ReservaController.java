package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.services.ReservaService;
import com.cosarbo.digitalizacion.services.UsuarioService;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservas")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicioCosturaService servicioService;

    // 1. Endpoint para obtener solo las fechas ocupadas (formato YYYY-MM-DD)
    // Esto es lo que usará el calendario para ponerse en gris
    @GetMapping("/ocupadas")
    public List<LocalDate> obtenerFechasOcupadas() {
        return reservaService.obtenerFechasOcupadas();
    }

    // 2. Endpoint para agendar una nueva cita
    // Recibe un JSON con idUsuario, idServicio y fechaReserva
    @PostMapping("/agendar")
    public ResponseEntity<?> agendarCita(@RequestBody Map<String, Object> payload) {
        try {
            Integer idUsuario = (Integer) payload.get("idUsuario");
            Integer idServicio = (Integer) payload.get("idServicio");
            String fechaStr = (String) payload.get("fechaReserva");
            LocalDate fecha = LocalDate.parse(fechaStr);

            // Buscamos las entidades completas para la relación
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            ServicioCostura servicio = servicioService.obtenerPorId(idServicio);

            if (usuario == null || servicio == null) {
                return ResponseEntity.badRequest().body("Usuario o Servicio no encontrado");
            }

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setUsuario(usuario);
            nuevaReserva.setServicio(servicio);
            nuevaReserva.setFechaReserva(fecha);
            nuevaReserva.setEstado("Confirmada");

            Reserva guardada = reservaService.agendarCita(nuevaReserva);
            return ResponseEntity.ok(guardada);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la reserva: " + e.getMessage());
        }
    }

    // 3. (Opcional) Listar todas las reservas para el Admin Dashboard
    @GetMapping
    public List<Reserva> listarTodas() {
        // Podrías crear este método en tu service para ver todas las citas en el Admin
        return reservaService.listarTodas(); 
    }
}