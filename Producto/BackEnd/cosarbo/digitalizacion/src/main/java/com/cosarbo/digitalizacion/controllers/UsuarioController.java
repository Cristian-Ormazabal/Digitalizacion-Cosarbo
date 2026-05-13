package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.dto.UsuarioRegistroDTO;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/correo/{correo}")
    public UsuarioDTO buscarPorCorreo(@PathVariable String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    @PostMapping
    public UsuarioDTO crear(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return usuarioService.crear(usuarioRegistroDTO);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Integer id, @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return usuarioService.actualizar(id, usuarioRegistroDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }

    @GetMapping("/total")
    public long contarTotalUsuarios() {
        return usuarioService.contarTotalUsuarios();
    }
}