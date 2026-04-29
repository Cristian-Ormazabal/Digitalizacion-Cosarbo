package com.cosarbo.digitalizacion.repositories;
import com.cosarbo.digitalizacion.entities.itemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface itemCarritoRepository extends JpaRepository<itemCarrito, Integer> {
}