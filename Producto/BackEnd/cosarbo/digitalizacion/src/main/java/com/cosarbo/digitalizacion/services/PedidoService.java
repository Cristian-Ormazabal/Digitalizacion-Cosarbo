package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Pedido;
import java.util.List;
import java.util.Map;

public interface PedidoService {
    // Listar todos para el Admin Panel
    List<Pedido> listarTodos();
    
    // Crear el registro oficial de la venta
    Pedido crearPedido(Integer idCarrito, Map<String, Object> datosEnvio, Double total);
    
    // Buscar uno específico
    Pedido obtenerPorId(Integer id);
}