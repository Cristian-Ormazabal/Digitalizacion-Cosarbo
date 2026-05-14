package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import java.util.List;
import java.util.Map;

public interface UsuarioService {

    // Métodos básicos de gestión de usuarios
    List<Usuario> listarTodos();
    
    Usuario guardar(Usuario usuario);
    
    Usuario obtenerPorId(Integer id);
    
    void eliminar(Integer id);

    /**
     * Procesa el cierre del carrito actual y la creación del nuevo.
     * @param idUsuario ID del usuario que realiza la compra.
     * @return UsuarioDTO con el nuevo idCarrito para el relevo en el frontend.
     */
    UsuarioDTO finalizarCompra(Integer idUsuario, Map<String, Object> datosEnvio);

    /**
     * Autentica al usuario y gestiona su carrito activo.
     * @param correo Email del usuario.
     * @param password Contraseña.
     * @return UsuarioDTO con la información de sesión y el carrito activo.
     */
    UsuarioDTO login(String correo, String password);

    UsuarioDTO registrarNuevoUsuario(Usuario usuario);
}