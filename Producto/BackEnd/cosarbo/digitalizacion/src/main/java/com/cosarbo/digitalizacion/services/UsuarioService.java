package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Integer id);
    Usuario guardar(Usuario usuario);
    void eliminar(Integer id);
}

