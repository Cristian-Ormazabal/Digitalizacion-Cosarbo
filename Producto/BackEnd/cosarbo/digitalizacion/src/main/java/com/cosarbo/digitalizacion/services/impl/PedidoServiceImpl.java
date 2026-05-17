package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Pedido;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.repositories.PedidoRepository;
import com.cosarbo.digitalizacion.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaVentaDesc();
    }

    @Override
    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Pedido crearPedido(Integer idCarrito, Map<String, Object> datosEnvio, Double total) {
        // Se busca el carrito que se está pagando
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Se crea la instancia de Pedido (La "Foto" de la venta)
        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);
        
        // Se mapean los datos que vienen desde el Checkout.jsx
        String nombreCompleto = datosEnvio.get("nombre") + " " + datosEnvio.get("apellidos");
        pedido.setNombreReceptor(nombreCompleto);
        pedido.setCorreoContacto((String) datosEnvio.get("correo"));
        pedido.setDireccion((String) datosEnvio.get("calle"));
        pedido.setComuna((String) datosEnvio.get("comuna"));
        pedido.setTotalPagado(total);

        // Se guarda en la tabla 'pedidos'
        return pedidoRepository.save(pedido);
    }
}