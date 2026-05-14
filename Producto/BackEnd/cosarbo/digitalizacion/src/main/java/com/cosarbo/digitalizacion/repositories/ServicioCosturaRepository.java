package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioCosturaRepository extends JpaRepository<ServicioCostura, Integer> {
    // Aquí puedes agregar métodos de búsqueda personalizados si lo necesitas a futuro
}