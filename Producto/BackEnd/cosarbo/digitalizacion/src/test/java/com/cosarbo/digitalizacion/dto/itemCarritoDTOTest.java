package com.cosarbo.digitalizacion.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class itemCarritoDTOTest {

    @Test
    void gettersYSettersFuncionan() {

        itemCarritoDTO dto =
                new itemCarritoDTO();

        dto.setIdCarrito(1);
        dto.setIdProducto(2);
        dto.setCantidad(3);
        dto.setSubTotal(500.0);

        assertEquals(1, dto.getIdCarrito());
        assertEquals(2, dto.getIdProducto());
        assertEquals(3, dto.getCantidad());
        assertEquals(500.0, dto.getSubTotal());
    }
}