package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.config.JwtService;
import com.cosarbo.digitalizacion.dto.UsuarioActualizacionDTO;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    // LISTAR TODOS
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

    // LOGIN 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String password = credentials.get("password");
        
        UsuarioDTO userDto = usuarioService.login(correo, password);
        
        if (userDto != null) {
            // Se genera el token usando el correo y el rol del usuario autenticado
            String token = jwtService.generateToken(userDto.getCorreo(), userDto.getRol());
            
            // Se crea una respuesta armada para el Frontend
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("idUsuario", userDto.getIdUsuario());
            response.put("nombre", userDto.getNombre());
            response.put("rol", userDto.getRol());
            response.put("correo", userDto.getCorreo());
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Correo o contraseña incorrectos");
        }
    }

    @PostMapping("/{id}/finalizar-compra")
    public ResponseEntity<UsuarioDTO> finalizarCompra(
        @PathVariable Integer id, 
        @RequestBody Map<String, Object> datosEnvio) {
        
        // Imprime para ver si llegan los datos de Maipú (DEBUG)
        System.out.println("Datos recibidos: " + datosEnvio);
        
        try {
            // Se pasan los datos al servicio
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
            // Se llama al servicio para manejar la lógica de creación
            UsuarioDTO usuarioCreado = usuarioService.registrarNuevoUsuario(nuevoUsuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException e) {
            // Si el correo ya existe o hay un error, se envia el mensaje
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable Integer id, 
            @RequestBody UsuarioActualizacionDTO actualizacionDTO) {
        try {
            // Se invoca el servicio para procesar la actualización
            usuarioService.actualizarUsuario(id, actualizacionDTO);
            
            // Se retorna una respuesta limpia indicando éxito
            return ResponseEntity.ok().body(Map.of("message", "¡Perfil actualizado con éxito! 🌸"));
        } catch (RuntimeException e) {
            // Si el usuario no existe o hay un error de negocio
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Error crítico de servidor
            return ResponseEntity.status(500).body(Map.of("message", "Error interno al actualizar el perfil."));
        }
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}