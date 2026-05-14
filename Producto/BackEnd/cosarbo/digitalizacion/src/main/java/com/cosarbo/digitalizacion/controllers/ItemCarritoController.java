package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.dto.itemCarritoDTO;
import com.cosarbo.digitalizacion.services.itemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items-carrito")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemCarritoController {

    @Autowired
    private itemCarritoService itemCarritoService;

    // AÑADIR PRODUCTO AL CARRITO (El que usa Catalogo.jsx)
    @PostMapping
    public ResponseEntity<itemCarrito> agregarItem(@RequestBody itemCarritoDTO itemDTO) {
        try {
            itemCarrito nuevoItem = itemCarritoService.agregarProducto(itemDTO);
            return ResponseEntity.ok(nuevoItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // OBTENER TODOS LOS ITEMS DE UN CARRITO ESPECÍFICO
    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<itemCarrito>> listarPorCarrito(@PathVariable Integer idCarrito) {
        return ResponseEntity.ok(itemCarritoService.listarPorCarrito(idCarrito));
    }

    // ELIMINAR UN ITEM DEL CARRITO
    @DeleteMapping("/{idItem}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Integer idItem) {
        itemCarritoService.eliminar(idItem);
        return ResponseEntity.noContent().build();
    }

    // ACTUALIZAR CANTIDAD (Botones + y - en la vista del carrito)
    @PutMapping("/{idItem}/cantidad")
    public ResponseEntity<itemCarrito> actualizarCantidad(
            @PathVariable Integer idItem, 
            @RequestParam Integer nuevaCantidad) {
        return ResponseEntity.ok(itemCarritoService.actualizarCantidad(idItem, nuevaCantidad));
    }
}