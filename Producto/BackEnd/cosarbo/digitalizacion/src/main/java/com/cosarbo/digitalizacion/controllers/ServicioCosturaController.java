package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicioscostura")
@CrossOrigin(origins = "*")
public class ServicioCosturaController {

    @Autowired
    private ServicioCosturaService servicioCosturaService;

    @GetMapping
    public List<ServicioCostura> listarTodos() {
        return servicioCosturaService.listarTodos();
    }

    @PostMapping
    public ServicioCostura guardar(@RequestBody ServicioCostura servicioCostura) {
        return servicioCosturaService.guardar(servicioCostura);
    }

    @PostMapping("/calcular")
    public void calcularValor(@RequestBody ServicioCostura servicioCostura) {
        servicioCosturaService.calcularValor(servicioCostura);
    }

    @DeleteMapping("/{idServicio}")
    public void eliminar(@PathVariable Integer idServicio) {
        servicioCosturaService.eliminar(idServicio);
    }
}