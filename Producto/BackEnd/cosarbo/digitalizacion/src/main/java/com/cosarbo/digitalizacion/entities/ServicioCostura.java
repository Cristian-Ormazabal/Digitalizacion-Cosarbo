package com.cosarbo.digitalizacion.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServicioCostura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;
    
    private String tipoPrenda;
    private Integer costo;

    // Metodo indicado en el diagrama
    public void calcularValor() {
        // Aqui ira la lógica más adelante
    }
}