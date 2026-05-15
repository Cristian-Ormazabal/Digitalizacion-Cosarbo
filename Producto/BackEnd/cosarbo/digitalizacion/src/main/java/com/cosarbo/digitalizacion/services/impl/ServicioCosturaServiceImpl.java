package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import com.cosarbo.digitalizacion.repositories.ServicioCosturaRepository;
import com.cosarbo.digitalizacion.services.ServicioCosturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioCosturaServiceImpl implements ServicioCosturaService {

    @Autowired
    private ServicioCosturaRepository repository;

    @Override
    public List<ServicioCostura> listarServicios() {
        return repository.findAll();
    }

    @Override
    public ServicioCostura guardarServicio(ServicioCostura servicio) {
        return repository.save(servicio);
    }

    @Override
    public ServicioCostura obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void eliminarServicio(Integer id) {
        repository.deleteById(id);
    }
}