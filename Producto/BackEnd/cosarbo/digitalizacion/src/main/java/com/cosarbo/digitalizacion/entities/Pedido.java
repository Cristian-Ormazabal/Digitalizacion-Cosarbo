package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    @JsonProperty("idPedido")
    private Integer idPedido;

    @OneToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @Column(name = "nombre_receptor", nullable = false, length = 150)
    @JsonProperty("nombreReceptor") 
    private String nombreReceptor;

    @Column(name = "correo_contacto", length = 100)
    private String correoContacto;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, length = 100)
    @JsonProperty("comuna") 
    private String comuna;

    @Column(name = "fecha_venta")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fechaVenta") 
    private LocalDateTime fechaVenta;

    @Column(name = "total_pagado")
    @JsonProperty("totalPagado") 
    private Double totalPagado;

    public Pedido() {
        this.fechaVenta = LocalDateTime.now(); 
    }

    public Pedido(Carrito carrito, String nombreReceptor, String correoContacto, String direccion, String comuna, Double totalPagado) {
        this.carrito = carrito;
        this.nombreReceptor = nombreReceptor;
        this.correoContacto = correoContacto;
        this.direccion = direccion;
        this.comuna = comuna;
        this.totalPagado = totalPagado;
        this.fechaVenta = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(Double totalPagado) {
        this.totalPagado = totalPagado;
    }
}