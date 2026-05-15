package com.cosarbo.digitalizacion.repositories;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface itemCarritoRepository extends JpaRepository<itemCarrito, Integer> {

    // Buscar todos los items de un carrito específico
    List<itemCarrito> findByCarrito(Carrito carrito);

    // Lógica para evitar duplicados: Busca si ya existe ese producto en ese carrito
    Optional<itemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);

    // Borrar todos los items de un carrito (útil al vaciar o finalizar compra)
    void deleteByCarrito(Carrito carrito);
}