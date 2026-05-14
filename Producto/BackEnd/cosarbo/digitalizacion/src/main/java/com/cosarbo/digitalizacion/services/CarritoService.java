package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Carrito;

public interface CarritoService {
    // Obtiene el carrito PENDIENTE del usuario o crea uno nuevo
    Carrito obtenerOCrearCarritoActivo(Integer usuarioId);
    
    // Procesa el pago, descuenta stock y marca como COMPLETADO
    void finalizarCompra(Integer carritoId);
    
    // Obtiene un carrito específico por su ID
    Carrito obtenerPorId(Integer id);
}