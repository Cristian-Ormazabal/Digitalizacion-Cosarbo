package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import com.cosarbo.digitalizacion.services.TicketContactoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketContactoControllerTest {

    @Mock
    private TicketContactoService ticketContactoService;

    @InjectMocks
    private TicketContactoController controller;

    @Test
    void deberiaListarTodosLosTickets() {

        List<TicketContacto> tickets = new ArrayList<>();

        when(ticketContactoService.listarTodos())
                .thenReturn(tickets);

        List<TicketContacto> resultado =
                controller.listarTodos();

        assertEquals(tickets, resultado);
    }

    @Test
    void deberiaGuardarTicket() {

        TicketContacto ticket = new TicketContacto();

        when(ticketContactoService.guardar(ticket))
                .thenReturn(ticket);

        TicketContacto resultado =
                controller.guardar(ticket);

        assertEquals(ticket, resultado);
    }

    @Test
    void deberiaEliminarTicket() {

        controller.eliminar(1);

        verify(ticketContactoService)
                .eliminar(1);
    }
}