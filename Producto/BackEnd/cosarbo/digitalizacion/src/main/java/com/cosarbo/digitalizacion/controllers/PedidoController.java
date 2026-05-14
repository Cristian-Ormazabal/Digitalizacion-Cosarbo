package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Pedido;
import com.cosarbo.digitalizacion.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Obtener todos los pedidos realizados en Cosarbo.
     * Los devuelve ordenados por fecha (del más reciente al más antiguo).
     */
    @GetMapping
    public List<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaVentaDesc();
    }

    /**
     * Buscar un pedido específico por su ID.
     * Útil si quieres una vista de detalle más profunda.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> ResponseEntity.ok().body(pedido))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * (Opcional) Eliminar un registro de pedido.
     * Solo usar en caso de pruebas o cancelaciones definitivas.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}