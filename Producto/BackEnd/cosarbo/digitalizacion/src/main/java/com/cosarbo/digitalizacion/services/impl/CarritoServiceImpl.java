package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.*;
import com.cosarbo.digitalizacion.repositories.*;
import com.cosarbo.digitalizacion.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public Carrito obtenerOCrearCarritoActivo(Integer usuarioId) {
        // Buscamos todos los carritos del usuario
        List<Carrito> carritos = carritoRepository.findByUsuario_IdUsuario(usuarioId);

        // Filtramos el que esté PENDIENTE
        return carritos.stream()
                .filter(c -> "PENDIENTE".equals(c.getEstado()))
                .findFirst()
                .orElseGet(() -> {
                    // Si no hay, creamos el relevo
                    Carrito nuevo = new Carrito();
                    Usuario user = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    nuevo.setUsuario(user);
                    nuevo.setEstado("PENDIENTE");
                    return carritoRepository.save(nuevo);
                });
    }

    @Override
    @Transactional
    public void finalizarCompra(Integer carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (!"PENDIENTE".equals(carrito.getEstado())) {
            throw new RuntimeException("El carrito ya fue procesado.");
        }

        // Lógica de descuento de stock
        for (itemCarrito item : carrito.getItems()) {
            Producto p = item.getProducto();
            if (p.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + p.getNombre());
            }
            p.setStock(p.getStock() - item.getCantidad());
            productoRepository.save(p);
        }

        // Cerramos el carrito
        carrito.setEstado("COMPLETADO");
        carritoRepository.save(carrito);
    }

    @Override
    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id).orElse(null);
    }
}