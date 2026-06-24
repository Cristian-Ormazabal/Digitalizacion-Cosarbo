package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Pedido;
import com.cosarbo.digitalizacion.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public List<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaVentaDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> ResponseEntity.ok().body(pedido))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mis-pedidos") // Enrutamiento exclusivo para el historial del cliente logueado
    public ResponseEntity<List<Pedido>> listarMisPedidos(Principal principal) {
        String correo = principal.getName(); 
        List<Pedido> misPedidos = pedidoRepository.findByCarritoUsuarioCorreoOrderByFechaVentaDesc(correo);
        return ResponseEntity.ok(misPedidos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}