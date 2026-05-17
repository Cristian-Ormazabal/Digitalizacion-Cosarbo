package com.cosarbo.digitalizacion.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicio_costura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioCostura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;

    @Column(name = "tipo_prenda", nullable = false)
    private String tipoPrenda;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "costo") 
    private Integer costo;

    @Column(name = "tiempo_estimado")
    private String tiempoEstimado;

    @Column(name = "estado_cupo")
    private String estadoCupo;
}