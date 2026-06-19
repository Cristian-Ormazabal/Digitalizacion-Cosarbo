package com.cosarbo.digitalizacion.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicioCosturaTest {

    @Test
    void constructorVacioDebeFuncionar() {

        ServicioCostura servicio = new ServicioCostura();

        assertNotNull(servicio);
    }

    @Test
    void constructorCompletoDebeAsignarValores() {

        ServicioCostura servicio = new ServicioCostura(
                1,
                "Pantalón",
                "Basta y ajuste",
                12000,
                "2 días"
        );

        assertEquals(1, servicio.getIdServicio());
        assertEquals("Pantalón", servicio.getTipoPrenda());
        assertEquals("Basta y ajuste", servicio.getDescripcion());
        assertEquals(12000, servicio.getCosto());
        assertEquals("2 días", servicio.getTiempoEstimado());
    }

    @Test
    void gettersYSettersDebenFuncionar() {

        ServicioCostura servicio = new ServicioCostura();

        servicio.setIdServicio(5);
        servicio.setTipoPrenda("Chaqueta");
        servicio.setDescripcion("Cambio de cierre");
        servicio.setCosto(15000);
        servicio.setTiempoEstimado("3 días");

        assertEquals(5, servicio.getIdServicio());
        assertEquals("Chaqueta", servicio.getTipoPrenda());
        assertEquals("Cambio de cierre", servicio.getDescripcion());
        assertEquals(15000, servicio.getCosto());
        assertEquals("3 días", servicio.getTiempoEstimado());
    }
}