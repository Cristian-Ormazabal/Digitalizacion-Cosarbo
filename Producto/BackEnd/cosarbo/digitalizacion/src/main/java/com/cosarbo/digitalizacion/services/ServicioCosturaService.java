package com.cosarbo.digitalizacion.services;

import com.cosarbo.digitalizacion.entities.ServicioCostura;
import java.util.List;

public interface ServicioCosturaService {
    List<ServicioCostura> listarServicios();
    ServicioCostura guardarServicio(ServicioCostura servicio);
    ServicioCostura obtenerPorId(Integer id);
    void eliminarServicio(Integer id);
}