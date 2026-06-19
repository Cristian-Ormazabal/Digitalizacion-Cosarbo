package com.cosarbo.digitalizacion.integration;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.repositories.ServicioCosturaRepository;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServicioCosturaIntegrationTest {

    @Autowired
    private ServicioCosturaService servicioCosturaService;

    @Autowired
    private ServicioCosturaRepository servicioCosturaRepository;

    @BeforeEach
    void limpiarBase() {
        servicioCosturaRepository.deleteAll();
    }

    @Test
    void deberiaGuardarServicio() {

        ServicioCostura servicio = new ServicioCostura();
        servicio.setTipoPrenda("Pantalón");
        servicio.setDescripcion("Basta de pantalón");
        servicio.setCosto(5000);
        servicio.setTiempoEstimado("2 días");

        ServicioCostura guardado =
                servicioCosturaService.guardarServicio(servicio);

        assertNotNull(guardado.getIdServicio());
        assertEquals("Pantalón", guardado.getTipoPrenda());
    }

    @Test
    void deberiaObtenerServicioPorId() {

        ServicioCostura servicio = new ServicioCostura();
        servicio.setTipoPrenda("Chaqueta");
        servicio.setDescripcion("Ajuste de mangas");
        servicio.setCosto(8000);
        servicio.setTiempoEstimado("3 días");

        ServicioCostura guardado =
                servicioCosturaRepository.save(servicio);

        ServicioCostura encontrado =
                servicioCosturaService.obtenerPorId(
                        guardado.getIdServicio());

        assertNotNull(encontrado);
        assertEquals("Chaqueta", encontrado.getTipoPrenda());
    }

    @Test
    void deberiaListarServicios() {

        ServicioCostura s1 = new ServicioCostura();
        s1.setTipoPrenda("Vestido");
        s1.setDescripcion("Ajuste");
        s1.setCosto(10000);
        s1.setTiempoEstimado("5 días");

        ServicioCostura s2 = new ServicioCostura();
        s2.setTipoPrenda("Camisa");
        s2.setDescripcion("Cambio de cuello");
        s2.setCosto(6000);
        s2.setTiempoEstimado("2 días");

        servicioCosturaRepository.save(s1);
        servicioCosturaRepository.save(s2);

        List<ServicioCostura> servicios =
                servicioCosturaService.listarServicios();

        assertEquals(2, servicios.size());
    }

    @Test
    void deberiaEliminarServicio() {

        ServicioCostura servicio = new ServicioCostura();
        servicio.setTipoPrenda("Falda");
        servicio.setDescripcion("Ajuste cintura");
        servicio.setCosto(7000);
        servicio.setTiempoEstimado("1 día");

        ServicioCostura guardado =
                servicioCosturaRepository.save(servicio);

        servicioCosturaService.eliminarServicio(
                guardado.getIdServicio());

        ServicioCostura eliminado =
                servicioCosturaService.obtenerPorId(
                        guardado.getIdServicio());

        assertNull(eliminado);
    }
}