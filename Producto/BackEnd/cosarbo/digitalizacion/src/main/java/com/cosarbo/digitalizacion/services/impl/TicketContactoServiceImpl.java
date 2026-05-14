package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import com.cosarbo.digitalizacion.repositories.TicketContactoRepository;
import com.cosarbo.digitalizacion.services.TicketContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketContactoServiceImpl implements TicketContactoService {

    @Autowired
    private TicketContactoRepository ticketContactoRepository;

    @Override
    public List<TicketContacto> listarTodos() {
        return ticketContactoRepository.findAll();
    }

    @Override
    public TicketContacto guardar(TicketContacto ticketContacto) {
        return ticketContactoRepository.save(ticketContacto);
    }

    @Override
    public void eliminar(Integer idTicket) {
        ticketContactoRepository.deleteById(idTicket);
    }
}