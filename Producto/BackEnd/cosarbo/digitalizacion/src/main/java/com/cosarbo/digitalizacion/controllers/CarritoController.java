package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{id}")
    public Carrito obtenerPorId(@PathVariable Integer id) {
        return carritoService.obtenerCarritoPorId(id);
    }

    @PostMapping
    public Carrito crear(@RequestBody Carrito carrito) {
        return carritoService.guardar(carrito);
    }

    @PostMapping("/calcular")
    public void calcularTotal(@RequestBody Carrito carrito) {
        carritoService.calcularTotal(carrito);
    }
}