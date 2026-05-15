package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    // @Column(columnDefinition = "TEXT")
    // private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    // @Column(name = "imagen_url")
    // private String imagenUrl;

    // @Column(length = 50)
    // private String categoria; // Ejemplo: 'Amigurumi', 'Accesorio', 'Lana'

    // Relación con los items del carrito (Opcional, para evitar recursividad en JSON)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<itemCarrito> items;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(Integer idProducto, String nombre, Double precio, Integer stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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

    // public String getDescripcion() {
    //     return descripcion;
    // }

    // public void setDescripcion(String descripcion) {
    //     this.descripcion = descripcion;
    // }

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

    // public String getImagenUrl() {
    //     return imagenUrl;
    // }

    // public void setImagenUrl(String imagenUrl) {
    //     this.imagenUrl = imagenUrl;
    // }

    // public String getCategoria() {
    //     return categoria;
    // }

    // public void setCategoria(String categoria) {
    //       this.categoria = categoria;
    // }

    public List<itemCarrito> getItems() {
        return items;
    }

    public void setItems(List<itemCarrito> items) {
        this.items = items;
    }
}