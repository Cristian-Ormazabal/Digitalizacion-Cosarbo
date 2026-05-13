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
    private ServicioCosturaRepository servicioCosturaRepository;

    @Override
    public List<ServicioCostura> listarTodos() {
        return servicioCosturaRepository.findAll();
    }

    @Override
    public ServicioCostura guardar(ServicioCostura servicioCostura) {
        return servicioCosturaRepository.save(servicioCostura);
    }

    @Override
    public void eliminar(Integer idServicio) {
        servicioCosturaRepository.deleteById(idServicio);
    }

    @Override
    public void calcularValor(ServicioCostura servicioCostura) {

        servicioCostura.calcularValor();
        servicioCosturaRepository.save(servicioCostura);
    }
}