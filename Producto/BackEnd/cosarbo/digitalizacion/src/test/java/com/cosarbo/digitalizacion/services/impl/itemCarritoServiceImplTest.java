package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.dto.itemCarritoDTO;
import com.cosarbo.digitalizacion.entities.*;
import com.cosarbo.digitalizacion.repositories.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class itemCarritoServiceImplTest {

    @Mock
    private itemCarritoRepository itemCarritoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private itemCarritoServiceImpl service;


        @Test
        void debeAgregarNuevoProductoAlCarrito() {

        itemCarritoDTO dto = new itemCarritoDTO();
        dto.setIdCarrito(1);
        dto.setIdProducto(1);
        dto.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setItems(new ArrayList<>());

        Producto producto = new Producto();
        producto.setPrecio(100.0);

        when(carritoRepository.findById(1))
                .thenReturn(Optional.of(carrito));

        when(productoRepository.findById(1))
                .thenReturn(Optional.of(producto));

        when(itemCarritoRepository.findByCarritoAndProducto(carrito, producto))
                .thenReturn(Optional.empty());

        when(itemCarritoRepository.save(any(itemCarrito.class)))
                .thenAnswer(i -> i.getArgument(0));

        itemCarrito resultado = service.agregarProducto(dto);

        assertEquals(2, resultado.getCantidad());
        assertEquals(200.0, resultado.getSubTotal());

        verify(itemCarritoRepository).save(any(itemCarrito.class));
        }

        @Test
        void debeAumentarCantidadSiProductoYaExiste() {

        itemCarritoDTO dto = new itemCarritoDTO();
        dto.setIdCarrito(1);
        dto.setIdProducto(1);
        dto.setCantidad(3);

        Carrito carrito = new Carrito();

        Producto producto = new Producto();
        producto.setPrecio(50.0);

        itemCarrito item = new itemCarrito();
        item.setCantidad(2);
        item.setProducto(producto);

        when(carritoRepository.findById(1))
                .thenReturn(Optional.of(carrito));

        when(productoRepository.findById(1))
                .thenReturn(Optional.of(producto));

        when(itemCarritoRepository.findByCarritoAndProducto(carrito, producto))
                .thenReturn(Optional.of(item));

        when(itemCarritoRepository.save(item))
                .thenReturn(item);

        itemCarrito resultado = service.agregarProducto(dto);

        assertEquals(5, resultado.getCantidad());
        assertEquals(250.0, resultado.getSubTotal());
        }

        @Test
        void debeLanzarErrorSiCarritoNoExiste() {

        itemCarritoDTO dto = new itemCarritoDTO();
        dto.setIdCarrito(1);

        when(carritoRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.agregarProducto(dto)
        );

        assertEquals("Carrito no encontrado", error.getMessage());
        }

        @Test
        void debeActualizarCantidadYSubtotal() {

        Producto producto = new Producto();
        producto.setPrecio(80.0);

        itemCarrito item = new itemCarrito();
        item.setProducto(producto);

        when(itemCarritoRepository.findById(1))
                .thenReturn(Optional.of(item));

        when(itemCarritoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        itemCarrito resultado =
                service.actualizarCantidad(1, 4);

        assertEquals(4, resultado.getCantidad());
        assertEquals(320.0, resultado.getSubTotal());
        }

        @Test
        void debeEliminarItemPorId() {

        service.eliminar(1);

        verify(itemCarritoRepository)
                .deleteById(1);
        }

        @Test
        void debeLanzarErrorSiProductoNoExiste() {

                itemCarritoDTO dto = new itemCarritoDTO();
                dto.setIdCarrito(1);
                dto.setIdProducto(99);

                Carrito carrito = new Carrito();

                when(carritoRepository.findById(1))
                        .thenReturn(Optional.of(carrito));

                when(productoRepository.findById(99))
                        .thenReturn(Optional.empty());

                RuntimeException error = assertThrows(
                        RuntimeException.class,
                        () -> service.agregarProducto(dto)
                );

                assertEquals("Producto no encontrado", error.getMessage());
        }
        @Test
        void debeListarItemsPorCarrito() {

                Carrito carrito = new Carrito();

                when(carritoRepository.findById(1))
                        .thenReturn(Optional.of(carrito));

                when(itemCarritoRepository.findByCarrito(carrito))
                        .thenReturn(new ArrayList<>());

                assertNotNull(service.listarPorCarrito(1));

                verify(itemCarritoRepository)
                        .findByCarrito(carrito);
        }

        @Test
                void vaciarCarritoDebeEliminarItems() {

                Carrito carrito = new Carrito();

                when(carritoRepository.findById(1))
                        .thenReturn(Optional.of(carrito));

                service.vaciarCarrito(1);

                verify(itemCarritoRepository)
                        .deleteByCarrito(carrito);
        }

        @Test
        void vaciarCarritoDebeLanzarErrorSiNoExiste() {

        when(carritoRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.vaciarCarrito(1)
        );

        assertEquals("Carrito no encontrado", error.getMessage());
        }
        @Test
        void actualizarCantidadDebeLanzarErrorSiItemNoExiste() {

        when(itemCarritoRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException error = assertThrows(
                RuntimeException.class,
                () -> service.actualizarCantidad(1, 3)
        );

        assertEquals("Item no encontrado", error.getMessage());
        }

        
}