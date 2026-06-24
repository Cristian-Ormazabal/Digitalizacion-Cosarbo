package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import com.cosarbo.digitalizacion.repositories.TicketContactoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketContactoServiceImplTest {

    @Mock
    private TicketContactoRepository ticketContactoRepository;

    @InjectMocks
    private TicketContactoServiceImpl service;

    @Test
    void debeListarTodosLosTickets() {

        when(ticketContactoRepository.findAll())
                .thenReturn(List.of(
                        new TicketContacto(),
                        new TicketContacto()
                ));

        List<TicketContacto> resultado =
                service.listarTodos();

        assertEquals(2, resultado.size());

        verify(ticketContactoRepository)
                .findAll();
    }

    @Test
    void debeGuardarTicket() {

        TicketContacto ticket =
                new TicketContacto();

        when(ticketContactoRepository.save(ticket))
                .thenReturn(ticket);

        TicketContacto resultado =
                service.guardar(ticket);

        assertNotNull(resultado);

        verify(ticketContactoRepository)
                .save(ticket);
    }

    @Test
    void debeEliminarTicket() {

        service.eliminar(1);

        verify(ticketContactoRepository)
                .deleteById(1);
    }
}