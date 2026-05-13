package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.dto.UsuarioRegistroDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listarTodos();
    UsuarioDTO buscarPorId(Integer id);
    UsuarioDTO buscarPorCorreo(String correo); 
    UsuarioDTO crear(UsuarioRegistroDTO usuarioRegistroDTO);
    UsuarioDTO actualizar(Integer id, UsuarioRegistroDTO usuarioRegistroDTO); 
    void eliminar(Integer id);
    long contarTotalUsuarios();
}