package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.dto.itemCarritoDTO;
import com.cosarbo.digitalizacion.repositories.itemCarritoRepository;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.itemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class itemCarritoServiceImpl implements itemCarritoService {

    @Autowired
    private itemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public itemCarrito agregarProducto(itemCarritoDTO itemDTO) {
        // 1. Verificar que el carrito existe
        Carrito carrito = carritoRepository.findById(itemDTO.getIdCarrito())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // 2. Verificar que el producto existe
        Producto producto = productoRepository.findById(itemDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 3. LÓGICA CLAVE: ¿El producto ya está en este carrito?
        Optional<itemCarrito> itemExistente = itemCarritoRepository
                .findByCarritoAndProducto(carrito, producto);

        if (itemExistente.isPresent()) {
            // Si ya existe, aumentamos la cantidad
            itemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + itemDTO.getCantidad());
            return itemCarritoRepository.save(item);
        } else {
            // Si no existe, creamos uno nuevo
            itemCarrito nuevoItem = new itemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(itemDTO.getCantidad());
            nuevoItem.setSubTotal(itemDTO.getSubTotal());
            return itemCarritoRepository.save(nuevoItem);
        }
    }

    @Override
    public List<itemCarrito> listarPorCarrito(Integer idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return itemCarritoRepository.findByCarrito(carrito);
    }

    @Override
    public itemCarrito actualizarCantidad(Integer idItem, Integer nuevaCantidad) {
        itemCarrito item = itemCarritoRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        item.setCantidad(nuevaCantidad);
        return itemCarritoRepository.save(item);
    }

    @Override
    public void eliminar(Integer idItem) {
        itemCarritoRepository.deleteById(idItem);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Integer idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        itemCarritoRepository.deleteByCarrito(carrito);
    }
}