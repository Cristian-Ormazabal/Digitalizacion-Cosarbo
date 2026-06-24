package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {


    List<Pedido> findAllByOrderByFechaVentaDesc();
    List<Pedido> findByCarritoUsuarioCorreoOrderByFechaVentaDesc(String correo);

}