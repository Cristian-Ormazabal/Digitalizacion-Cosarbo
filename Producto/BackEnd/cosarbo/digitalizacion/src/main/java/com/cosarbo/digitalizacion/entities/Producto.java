package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    @JsonProperty("idProducto")
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(nullable = false)
    @JsonProperty("precio")
    private Double precio;

    @Column(nullable = false)
    @JsonProperty("stock")
    private Integer stock;

    @Column(name = "imagen")
    @JsonProperty("imagen")
    private String imagen;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<itemCarrito> items;

    public Producto() {
    }

    public Producto(Integer idProducto, String nombre, Double precio, Integer stock, String descripcion, String imagen) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    // Getters y Setters

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<itemCarrito> getItems() {
        return items;
    }

    public void setItems(List<itemCarrito> items) {
        this.items = items;
    }
}