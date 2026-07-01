package com.cosarbo.digitalizacion.controllers;
import com.cosarbo.digitalizacion.config.JwtService;
import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.services.CarritoService;
import com.cosarbo.digitalizacion.services.itemCarritoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarritoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;
    
    @MockBean
    private itemCarritoService itemCarritoService;
   
    @MockBean
    private JwtService jwtService;
    


//Endpoint POST /api/v1/carrito/usuario/{usuarioId}
@Test
void debeObtenerOCrearCarrito() throws Exception {

    Carrito carrito = new Carrito();

    when(carritoService.obtenerOCrearCarritoActivo(1))
            .thenReturn(carrito);

    mockMvc.perform(post("/api/v1/carrito/usuario/1"))
            .andExpect(status().isOk());

    verify(carritoService)
            .obtenerOCrearCarritoActivo(1);
}

//Cuando el Service falla, responde 500
@Test
void debeRetornarErrorInternoAlCrearCarrito() throws Exception {

    when(carritoService.obtenerOCrearCarritoActivo(1))
            .thenThrow(new RuntimeException());

    mockMvc.perform(post("/api/v1/carrito/usuario/1"))
            .andExpect(status().isInternalServerError());
}

//Endpoint: GET /api/v1/carrito/{id}

@Test
void debeObtenerCarritoPorId() throws Exception {

    Carrito carrito = new Carrito();

    when(carritoService.obtenerPorId(1))
            .thenReturn(carrito);

    mockMvc.perform(get("/api/v1/carrito/1"))
            .andExpect(status().isOk());
}

//Debe devolver 404.
@Test
void debeRetornarNotFoundSiCarritoNoExiste() throws Exception {

    when(carritoService.obtenerPorId(1))
            .thenReturn(null);

    mockMvc.perform(get("/api/v1/carrito/1"))
            .andExpect(status().isNotFound());
}

//Endpoint: POST /api/v1/carrito/{id}/finalizar

@Test
void debeFinalizarCompraCorrectamente() throws Exception {

    doNothing()
            .when(carritoService)
            .finalizarCompra(1);

    mockMvc.perform(post("/api/v1/carrito/1/finalizar"))
            .andExpect(status().isOk());

    verify(carritoService)
            .finalizarCompra(1);
}

//Controller responde 400.

@Test
void debeRetornarBadRequestSiHayErrorDeStock() throws Exception {

    doThrow(new RuntimeException("No hay stock"))
            .when(carritoService)
            .finalizarCompra(1);

    mockMvc.perform(post("/api/v1/carrito/1/finalizar"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("No hay stock"));
}

}