package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.repositories.itemCarritoRepository;
import com.cosarbo.digitalizacion.services.itemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class itemCarritoServiceImpl implements itemCarritoService {

    @Autowired
    private itemCarritoRepository itemCarritoRepository;

    @Override
    public itemCarrito guardar(itemCarrito item) {
        return itemCarritoRepository.save(item);
    }

    @Override
    public void eliminar(Integer id) {
        itemCarritoRepository.deleteById(id);
    }
}