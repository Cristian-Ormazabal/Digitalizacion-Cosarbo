package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.TicketContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketContactoRepository extends JpaRepository<TicketContacto, Integer> {
}