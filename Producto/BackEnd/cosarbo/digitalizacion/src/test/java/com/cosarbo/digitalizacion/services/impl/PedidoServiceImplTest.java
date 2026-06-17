package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Pedido;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.repositories.PedidoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private PedidoServiceImpl service;



        @Test
        void debeListarTodosLosPedidos() {

        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(new Pedido());

        when(pedidoRepository.findAllByOrderByFechaVentaDesc())
                .thenReturn(pedidos);

        List<Pedido> resultado = service.listarTodos();

        assertEquals(1, resultado.size());

        verify(pedidoRepository)
                .findAllByOrderByFechaVentaDesc();
        }

        @Test
        void debeObtenerPedidoPorId() {

        Pedido pedido = new Pedido();

        when(pedidoRepository.findById(1))
                .thenReturn(Optional.of(pedido));

        Pedido resultado = service.obtenerPorId(1);

        assertNotNull(resultado);

        verify(pedidoRepository).findById(1);
        }

        @Test
        void debeRetornarNullSiPedidoNoExiste() {

        when(pedidoRepository.findById(1))
                .thenReturn(Optional.empty());

        Pedido resultado = service.obtenerPorId(1);

        assertNull(resultado);
        }

        @Test
        void debeCrearPedidoCorrectamente() {

        Carrito carrito = new Carrito();

        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", "Juan");
        datos.put("apellidos", "Perez");
        datos.put("correo", "juan@test.com");
        datos.put("calle", "Av. Siempre Viva 123");
        datos.put("comuna", "Santiago");

        when(carritoRepository.findById(1))
                .thenReturn(Optional.of(carrito));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = service.crearPedido(1, datos, 25000.0);

        assertEquals("Juan Perez", resultado.getNombreReceptor());
        assertEquals("juan@test.com", resultado.getCorreoContacto());
        assertEquals("Av. Siempre Viva 123", resultado.getDireccion());
        assertEquals("Santiago", resultado.getComuna());
        assertEquals(25000.0, resultado.getTotalPagado());
        assertEquals(carrito, resultado.getCarrito());

        verify(pedidoRepository)
                .save(any(Pedido.class));
        }

        @Test
        void debeLanzarErrorSiCarritoNoExiste() {

        when(carritoRepository.findById(1))
                .thenReturn(Optional.empty());

        Map<String, Object> datos = new HashMap<>();

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.crearPedido(1, datos, 10000.0)
        );

        assertEquals("Carrito no encontrado", error.getMessage());

        verify(pedidoRepository, never())
                .save(any(Pedido.class));
        }

}