package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;

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
class ServicioCosturaControllerTest {

    @Mock
    private ServicioCosturaService service;

    @InjectMocks
    private ServicioCosturaController controller;


    @Test
    void deberiaListarServicios() {

        List<ServicioCostura> servicios = new ArrayList<>();

        when(service.listarServicios())
                .thenReturn(servicios);

        List<ServicioCostura> respuesta = controller.listar();

        assertEquals(servicios, respuesta);
    }


    @Test
    void deberiaCrearServicio() {

        ServicioCostura servicio = new ServicioCostura();

        when(service.guardarServicio(servicio))
                .thenReturn(servicio);

        ServicioCostura respuesta = controller.crear(servicio);

        assertEquals(servicio, respuesta);
    }


    @Test
    void deberiaObtenerServicioPorId() {

        ServicioCostura servicio = new ServicioCostura();

        when(service.obtenerPorId(1))
                .thenReturn(servicio);

        var respuesta = controller.obtener(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(servicio, respuesta.getBody());
    }


    @Test
    void deberiaRetornar404SiServicioNoExiste() {

        when(service.obtenerPorId(99))
                .thenReturn(null);

        var respuesta = controller.obtener(99);

        assertEquals(404, respuesta.getStatusCode().value());
    }


    @Test
    void deberiaEliminarServicio() {

        var respuesta = controller.eliminar(1);

        verify(service).eliminarServicio(1);

        assertEquals(204, respuesta.getStatusCode().value());
    }
}