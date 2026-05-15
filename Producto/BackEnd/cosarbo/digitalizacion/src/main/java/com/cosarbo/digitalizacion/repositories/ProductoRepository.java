package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // // Buscar amigurumis por categoría (ej: 'Navidad', 'Animales')
    // List<Producto> findByCategoria(String categoria);

    // Buscar productos por nombre ignorando mayúsculas/minúsculas
    // Ejemplo: 'oso' encontrará 'Oso de lana'
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Opcional: Listar productos que tengan stock disponible
    List<Producto> findByStockGreaterThan(Integer stock);
}