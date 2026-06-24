package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.services.CarritoService;
import com.cosarbo.digitalizacion.services.itemCarritoService;

import com.cosarbo.digitalizacion.dto.itemCarritoDTO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private itemCarritoService itemCarritoService;

    // 1. OBTENER O CREAR EL CARRITO ACTIVO
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> obtenerOCrear(@PathVariable Integer usuarioId) {
        try {
            Carrito carrito = carritoService.obtenerOCrearCarritoActivo(usuarioId);
            return ResponseEntity.ok(carrito);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. OBTENER CARRITO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerPorId(@PathVariable Integer id) {
        Carrito carrito = carritoService.obtenerPorId(id);
        if (carrito != null) {
            return ResponseEntity.ok(carrito);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. FINALIZAR COMPRA
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarCompra(@PathVariable Integer id) {
        try {
            carritoService.finalizarCompra(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Retorna el error específico (ej: "No hay stock suficiente")
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la compra.");
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProducto(@RequestBody Map<String, Object> payload) {
        try {
            Integer idCarrito = Integer.parseInt(payload.get("idCarrito").toString());
            Integer idProducto = Integer.parseInt(payload.get("idProducto").toString());
            Integer cantidad = Integer.parseInt(payload.get("cantidad").toString());

            itemCarritoDTO nuevoItemDTO = new itemCarritoDTO();
            nuevoItemDTO.setIdCarrito(idCarrito);
            nuevoItemDTO.setIdProducto(idProducto);
            nuevoItemDTO.setCantidad(cantidad);

            // Se guarda el ítem de forma persistente
            itemCarritoService.agregarProducto(nuevoItemDTO);
            
            List<itemCarrito> itemsActualizados = itemCarritoService.listarPorCarrito(idCarrito);
            
            // Se retornan los ítems directamente para que React dibuje la verdad
            return ResponseEntity.ok(itemsActualizados);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar: " + e.getMessage());
        }
    }
}