package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    //Aun nada
}