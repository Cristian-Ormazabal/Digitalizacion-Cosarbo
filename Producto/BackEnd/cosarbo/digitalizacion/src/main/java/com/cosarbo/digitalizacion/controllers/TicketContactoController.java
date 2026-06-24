package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import com.cosarbo.digitalizacion.services.TicketContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketContactoController {

    @Autowired
    private TicketContactoService ticketContactoService;

    @GetMapping
    public List<TicketContacto> listarTodos() {
        return ticketContactoService.listarTodos();
    }

    @PostMapping
    public TicketContacto guardar(@RequestBody TicketContacto ticketContacto) {
        return ticketContactoService.guardar(ticketContacto);
    }

    @DeleteMapping("/{idTicket}")
    public void eliminar(@PathVariable Integer idTicket) {
        ticketContactoService.eliminar(idTicket);
    }
}