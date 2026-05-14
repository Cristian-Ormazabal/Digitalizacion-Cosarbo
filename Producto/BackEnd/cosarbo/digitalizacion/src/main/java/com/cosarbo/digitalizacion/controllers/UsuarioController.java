package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LISTAR TODOS (Útil para pruebas o panel admin)
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // OBTENER UNO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // REGISTRO DE USUARIO
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
    }

    // LOGIN (Devuelve el UsuarioDTO con el idCarrito activo)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String password = credentials.get("password");
        
        UsuarioDTO userDto = usuarioService.login(correo, password);
        
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Correo o contraseña incorrectos");
        }
    }

    @PostMapping("/{id}/finalizar-compra")
    public ResponseEntity<UsuarioDTO> finalizarCompra(
        @PathVariable Integer id, 
        @RequestBody Map<String, Object> datosEnvio) { // Agregamos @RequestBody
        
        // Podemos imprimir para ver si llegan los datos de Maipú
        System.out.println("Datos recibidos: " + datosEnvio);
        
        try {
            // Pasamos los datos al servicio
            UsuarioDTO resultado = usuarioService.finalizarCompra(id, datosEnvio);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        try {
            // Llamamos al servicio para manejar la lógica de creación
            UsuarioDTO usuarioCreado = usuarioService.registrarNuevoUsuario(nuevoUsuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException e) {
            // Si el correo ya existe o hay un error, enviamos el mensaje
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}