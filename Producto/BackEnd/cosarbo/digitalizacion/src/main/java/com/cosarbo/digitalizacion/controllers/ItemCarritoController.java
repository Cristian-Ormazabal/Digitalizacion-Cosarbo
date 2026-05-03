package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.services.itemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemCarritoController {

    @Autowired
    private itemCarritoService itemCarritoService;

    @PostMapping
    public itemCarrito guardar(@RequestBody itemCarrito item) {
        return itemCarritoService.guardar(item);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        itemCarritoService.eliminar(id);
    }
}