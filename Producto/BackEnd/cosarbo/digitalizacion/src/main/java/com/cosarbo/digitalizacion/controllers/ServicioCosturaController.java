package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios-costura")
public class ServicioCosturaController {

    @Autowired
    private ServicioCosturaService service;

    @GetMapping
    public List<ServicioCostura> listar() {
        return service.listarServicios();
    }

    @PostMapping
    public ServicioCostura crear(@RequestBody ServicioCostura servicio) {
        return service.guardarServicio(servicio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioCostura> obtener(@PathVariable Integer id) {
        ServicioCostura servicio = service.obtenerPorId(id);
        return servicio != null ? ResponseEntity.ok(servicio) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}