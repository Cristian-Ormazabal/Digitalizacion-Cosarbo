package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Producto;
import java.util.List;

public interface ProductoService {

    // Obtener la lista completa para el catálogo
    List<Producto> listarTodos();

    // Buscar un producto específico (útil para la vista de detalle)
    Producto obtenerPorId(Integer id);

    // Guardar o actualizar un producto (para el panel de admin)
    Producto guardar(Producto producto);

    // Eliminar un producto
    void eliminar(Integer id);

    // // Métodos de búsqueda que definimos en el Repository
    // List<Producto> listarPorCategoria(String categoria);
    
    List<Producto> buscarPorNombre(String nombre);
}