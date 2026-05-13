package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.dto.UsuarioRegistroDTO;
import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.repositories.UsuarioRepository;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    // TU BUENA PRÁCTICA: Inyección por constructor (Nivel Profesional)
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO buscarPorId(Integer id) {
        // TU BUENA PRÁCTICA: Manejo de errores con orElseThrow
        return usuarioRepository.findById(id)
                .map(this::mapearADto)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public UsuarioDTO buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(this::mapearADto)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    public UsuarioDTO crear(UsuarioRegistroDTO dto) {
        // TU BUENA PRÁCTICA: Validar si viene sin rol. (Le ponemos "USER" para Spring Security)
        if (dto.getRol() == null || dto.getRol().isBlank()) {
            dto.setRol("USER");
        }

        Usuario nuevoUsuario = mapearAEntidad(dto);
        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        return mapearADto(guardado);
    }

    @Override
    public UsuarioDTO actualizar(Integer id, UsuarioRegistroDTO dto) {
        // TU BUENA PRÁCTICA: Actualizar solo lo que corresponde
        return usuarioRepository.findById(id).map(ex -> {
            ex.setNombre(dto.getNombre());
            ex.setCorreo(dto.getCorreo());
            
            // Si nos envían una contraseña nueva, la actualizamos
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                ex.setPassword(dto.getPassword());
            }
            
            if (dto.getRol() != null && !dto.getRol().isBlank()) {
                ex.setRol(dto.getRol());
            }
            
            Usuario actualizado = usuarioRepository.save(ex);
            return mapearADto(actualizado);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado para actualizar"));
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE TRANSFORMACIÓN (MAPEO)
    // ==========================================
    private UsuarioDTO mapearADto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());

        if (usuario.getTickets() != null) {
            dto.setIdsTickets(usuario.getTickets().stream()
                    .map(ticket -> ticket.getIdTicket())
                    .collect(Collectors.toList()));
        }

        if (usuario.getCarrito() != null) {
            dto.setIdCarrito(usuario.getCarrito().getIdCarrito());
        }
        return dto;
    }

    private Usuario mapearAEntidad(UsuarioRegistroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPassword(dto.getPassword()); 
        usuario.setRol(dto.getRol());
        return usuario;
    }
}