package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.dto.UsuarioActualizacionDTO;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import java.util.List;
import java.util.Map;

public interface UsuarioService {

    // Métodos básicos de gestión de usuarios
    List<Usuario> listarTodos();
    
    Usuario guardar(Usuario usuario);
    
    Usuario obtenerPorId(Integer id);
    
    void eliminar(Integer id);

    UsuarioDTO finalizarCompra(Integer idUsuario, Map<String, Object> datosEnvio);

    UsuarioDTO login(String correo, String password);

    UsuarioDTO registrarNuevoUsuario(Usuario usuario);

    void actualizarUsuario(Integer idUsuario, UsuarioActualizacionDTO dto);
}