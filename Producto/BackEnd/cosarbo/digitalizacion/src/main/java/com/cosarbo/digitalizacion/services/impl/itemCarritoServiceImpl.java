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
        // Verificar que el carrito existe
        Carrito carrito = carritoRepository.findById(itemDTO.getIdCarrito())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Verificar que el producto existe
        Producto producto = productoRepository.findById(itemDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si el producto ya existe en este carrito para evitar duplicados
        Optional<itemCarrito> itemExistente = itemCarritoRepository
                .findByCarritoAndProducto(carrito, producto);

        if (itemExistente.isPresent()) {
            // Si el producto ya está en el carrito, se acumula la cantidad
            itemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + itemDTO.getCantidad();
            item.setCantidad(nuevaCantidad);
            
            // Se multiplica el precio real de la BD por la nueva cantidad acumulada
            item.setSubTotal(producto.getPrecio() * nuevaCantidad);
            
            return itemCarritoRepository.save(item);
        } 
        else {
            itemCarrito nuevoItem = new itemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(itemDTO.getCantidad());
            nuevoItem.setSubTotal(producto.getPrecio() * itemDTO.getCantidad());
            
            // Se avisa al carrito en memoria que tiene un nuevo ítem
            carrito.getItems().add(nuevoItem); 
            
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
    @Transactional
    public itemCarrito actualizarCantidad(Integer idItem, Integer nuevaCantidad) {
        itemCarrito item = itemCarritoRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        
        item.setCantidad(nuevaCantidad);
        
        // Si se usan botones de más/menos en la vista del carrito, se recalcula el subtotal
        if (item.getProducto() != null) {
            item.setSubTotal(item.getProducto().getPrecio() * nuevaCantidad);
        }
        
        return itemCarritoRepository.save(item);
    }

    @Override
    @Transactional
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