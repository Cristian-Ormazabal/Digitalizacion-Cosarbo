package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import java.util.List;

public interface TicketContactoService {
    List<TicketContacto> listarTodos();
    TicketContacto guardar(TicketContacto ticketContacto);
    void eliminar(Integer idTicket);
}