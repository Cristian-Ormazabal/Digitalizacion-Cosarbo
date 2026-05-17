package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    
    // Busca todos los carritos de un usuario
    List<Carrito> findByUsuario_IdUsuario(Integer idUsuario);
    Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, String estadoPedido);
    // Busca todos los carritos que ya fueron pagados
    List<Carrito> findByEstadoOrderByEstadoDesc(String estadoPedido);
}