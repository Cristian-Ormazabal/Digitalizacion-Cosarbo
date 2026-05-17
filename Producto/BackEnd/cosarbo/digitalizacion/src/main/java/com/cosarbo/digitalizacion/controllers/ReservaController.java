package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Reserva;
import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.repositories.ReservaRepository;
import com.cosarbo.digitalizacion.services.ReservaService;
import com.cosarbo.digitalizacion.services.UsuarioService;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private ReservaRepository reservaRepository; 

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicioCosturaService servicioService;

    @GetMapping("/ocupadas")
    public ResponseEntity<List<LocalDate>> obtenerFechasOcupadas() {
        return ResponseEntity.ok(reservaService.obtenerDiasAgotados()); 
    }

    @GetMapping("/horas-ocupadas")
    public ResponseEntity<List<String>> obtenerHorasOcupadas(@RequestParam("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return ResponseEntity.ok(reservaService.obtenerHorasOcupadasPorDia(fecha));
    }

    @PostMapping("/agendar")
    public ResponseEntity<?> agendarCita(@RequestBody Map<String, Object> payload) {
        try {
            Integer idUsuario = (Integer) payload.get("idUsuario");
            Integer idServicio = (Integer) payload.get("idServicio");
            String fechaStr = (String) payload.get("fechaReserva");
            LocalDate fecha = LocalDate.parse(fechaStr);
            String horaReserva = (String) payload.get("horaReserva"); 

            if (horaReserva == null || horaReserva.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar un horario para la atención");
            }

            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            ServicioCostura servicio = servicioService.obtenerPorId(idServicio);

            if (usuario == null || servicio == null) {
                return ResponseEntity.badRequest().body("Usuario o Servicio no encontrado");
            }

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setUsuario(usuario);
            nuevaReserva.setServicio(servicio);
            nuevaReserva.setFechaReserva(fecha);
            nuevaReserva.setHoraReserva(horaReserva); 
            nuevaReserva.setEstado("Confirmada");

            Reserva guardada = reservaService.agendarReserva(nuevaReserva);
            return ResponseEntity.ok(guardada);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la reserva: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas(); 
    }

    // Llama a la consulta nativa del repositorio sin depender de firmas inexistentes en el service
    @GetMapping("/mis-reservas")
    public ResponseEntity<List<Reserva>> listarMisReservas(Principal principal) {
        String correo = principal.getName();
        List<Reserva> misReservas = reservaRepository.findByUsuarioCorreoOrderByFechaReservaDesc(correo);
        return ResponseEntity.ok(misReservas);
    }
}