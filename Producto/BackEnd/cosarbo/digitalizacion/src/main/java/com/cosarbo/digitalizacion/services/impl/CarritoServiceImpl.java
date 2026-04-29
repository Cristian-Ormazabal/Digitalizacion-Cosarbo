package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public Carrito obtenerCarritoPorId(Integer id) {
        return carritoRepository.findById(id).orElse(null);
    }

    @Override
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void calcularTotal(Carrito carrito) {
        int total = 0;
        if (carrito.getItems() != null) {
            for (itemCarrito item : carrito.getItems()) {
                // Cantidad x Precio del producto asociado al item
                total += item.getCantidad() * item.getProducto().getPrecio();
            }
        }
        carrito.setTotal(total);
        carritoRepository.save(carrito);
    }
}