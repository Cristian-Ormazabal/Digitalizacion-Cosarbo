package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
@CrossOrigin(origins = {"http://localhost:5173", "https://tu-app-cosarbo.netlify.app"})
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // 1. OBTENER O CREAR EL CARRITO ACTIVO
    // Este es el endpoint que React llamará al cargar la página o al loguearse.
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
    // Útil para refrescar la vista cuando ya conocemos el ID del carrito.
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerPorId(@PathVariable Integer id) {
        Carrito carrito = carritoService.obtenerPorId(id);
        if (carrito != null) {
            return ResponseEntity.ok(carrito);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. FINALIZAR COMPRA
    // Este endpoint ejecuta el descuento de stock y marca el carrito como COMPLETADO.
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
}