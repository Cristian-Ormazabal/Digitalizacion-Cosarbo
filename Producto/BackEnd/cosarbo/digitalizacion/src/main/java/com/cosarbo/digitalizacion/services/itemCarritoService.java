package com.cosarbo.digitalizacion.services;
import com.cosarbo.digitalizacion.entities.itemCarrito;

public interface itemCarritoService {
    itemCarrito guardar(itemCarrito item);
    void eliminar(Integer id);
}

