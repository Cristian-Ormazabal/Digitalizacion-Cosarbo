package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.dto.itemCarritoDTO;
import java.util.List;

public interface itemCarritoService {
    
    // El método principal que usará el Catálogo
    itemCarrito agregarProducto(itemCarritoDTO itemDTO);
    
    List<itemCarrito> listarPorCarrito(Integer idCarrito);
    
    itemCarrito actualizarCantidad(Integer idItem, Integer nuevaCantidad);
    
    void eliminar(Integer idItem);
    
    void vaciarCarrito(Integer idCarrito);
}