package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import java.util.List;

public interface ServicioCosturaService {
    List<ServicioCostura> listarTodos();
    ServicioCostura guardar(ServicioCostura servicioCostura);
    void eliminar(Integer idServicio);
    void calcularValor(ServicioCostura servicioCostura); 
}