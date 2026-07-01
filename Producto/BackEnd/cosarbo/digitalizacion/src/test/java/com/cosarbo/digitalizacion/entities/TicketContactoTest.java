package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketContactoTest {

    @Test
    void debeProbarGettersYSetters() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        TicketContacto ticket =
                new TicketContacto();

        ticket.setIdTicket(10);
        ticket.setNombreUsuario("Pedro");
        ticket.setMensaje("Hola");
        ticket.setUsuario(usuario);

        assertEquals(
                10,
                ticket.getIdTicket()
        );

        assertEquals(
                "Pedro",
                ticket.getNombreUsuario()
        );

        assertEquals(
                "Hola",
                ticket.getMensaje()
        );

        assertEquals(
                usuario,
                ticket.getUsuario()
        );
    }

    @Test
    void debeProbarConstructorCompleto() {

        Usuario usuario = new Usuario();

        TicketContacto ticket =
                new TicketContacto(
                        1,
                        "Pedro",
                        "Mensaje",
                        usuario
                );

        assertEquals(
                1,
                ticket.getIdTicket()
        );

        assertEquals(
                "Pedro",
                ticket.getNombreUsuario()
        );

        assertEquals(
                "Mensaje",
                ticket.getMensaje()
        );

        assertEquals(
                usuario,
                ticket.getUsuario()
        );
    }

    @Test
    void debeProbarNoArgsConstructor() {

        TicketContacto ticket =
                new TicketContacto();

        assertNotNull(ticket);
    }
}