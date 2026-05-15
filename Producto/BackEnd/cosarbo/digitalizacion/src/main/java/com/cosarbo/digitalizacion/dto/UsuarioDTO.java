package com.cosarbo.digitalizacion.dto;

/**
 * Data Transfer Object para Usuario.
 * Se utiliza para enviar información al Frontend de forma segura,
 * omitiendo datos sensibles como la contraseña.
 */
public class UsuarioDTO {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private Integer idCarrito; // ID del carrito con estado 'PENDIENTE'

    // Constructor vacío (necesario para la serialización de JSON)
    public UsuarioDTO() {
    }

    // Constructor con parámetros (opcional, por si quieres instanciarlo rápido)
    public UsuarioDTO(Integer idUsuario, String nombre, String correo, String rol, Integer idCarrito) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.idCarrito = idCarrito;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }
}