package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios") // Esta será la URL base
@CrossOrigin(origins = "*") // Esto permitirá que React se conecte sin bloqueos
public class UsuarioController {

    @Autowired
    private UsuarioService UsuarioService;

    @GetMapping
    public List<Usuario> listarTodos() {
        return UsuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> buscarPorId(@PathVariable Integer id) {
        return UsuarioService.buscarPorId(id);
    }

    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {
        return UsuarioService.guardar(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        UsuarioService.eliminar(id);
    }
}