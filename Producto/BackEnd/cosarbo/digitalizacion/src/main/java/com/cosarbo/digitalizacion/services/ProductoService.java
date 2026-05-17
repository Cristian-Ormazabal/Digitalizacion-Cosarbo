package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Producto;
import java.util.List;

public interface ProductoService {

    // Obtener la lista completa para el catálogo
    List<Producto> listarTodos();

    // Buscar un producto específico 
    Producto obtenerPorId(Integer id);

    // Guardar o actualizar un producto
    Producto guardar(Producto producto);

    // Eliminar un producto
    void eliminar(Integer id);
    
    List<Producto> buscarPorNombre(String nombre);
}