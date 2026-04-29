package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.Producto;
import java.util.List;
public interface ProductoService {
    List<Producto> listarTodos();
    Producto guardar(Producto producto);
    void eliminar(Integer id);
}
