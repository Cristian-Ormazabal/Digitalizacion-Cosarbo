package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Carrito;

public interface CarritoService {
    Carrito obtenerCarritoPorId(Integer id);
    Carrito guardar(Carrito carrito);
    void calcularTotal(Carrito carrito);
}