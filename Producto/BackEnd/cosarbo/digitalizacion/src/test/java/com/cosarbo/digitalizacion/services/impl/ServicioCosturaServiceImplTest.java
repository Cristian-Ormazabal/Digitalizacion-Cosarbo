package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.repositories.ServicioCosturaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioCosturaServiceImplTest {

    @Mock
    private ServicioCosturaRepository repository;

    @InjectMocks
    private ServicioCosturaServiceImpl service;



    @Test
    void debeListarTodosLosServicios() {

        List<ServicioCostura> servicios = List.of(
                new ServicioCostura(),
                new ServicioCostura()
        );

        when(repository.findAll())
                .thenReturn(servicios);

        List<ServicioCostura> resultado = service.listarServicios();

        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }

    @Test
    void debeGuardarServicio() {

        ServicioCostura servicio = new ServicioCostura();

        when(repository.save(servicio))
                .thenReturn(servicio);

        ServicioCostura resultado = service.guardarServicio(servicio);

        assertNotNull(resultado);

        verify(repository).save(servicio);
    }

    @Test
    void debeObtenerServicioPorId() {

        ServicioCostura servicio = new ServicioCostura();

        when(repository.findById(1))
                .thenReturn(Optional.of(servicio));

        ServicioCostura resultado = service.obtenerPorId(1);

        assertNotNull(resultado);

        verify(repository).findById(1);
    }

    @Test
    void debeRetornarNullSiServicioNoExiste() {

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        ServicioCostura resultado = service.obtenerPorId(1);

        assertNull(resultado);
    }

    @Test
    void debeEliminarServicioPorId() {

        service.eliminarServicio(1);

        verify(repository).deleteById(1);
    }

}