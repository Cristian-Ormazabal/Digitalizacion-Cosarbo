package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.repositories.itemCarritoRepository;
import com.cosarbo.digitalizacion.repositories.UsuarioRepository;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.PedidoService;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private itemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioDTO finalizarCompra(Integer idUsuario, Map<String, Object> datosEnvio) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carritoActual = carritoRepository.findByUsuarioAndEstado(usuario, "PENDIENTE")
                .orElseThrow(() -> new RuntimeException("No hay carrito activo"));

        List<itemCarrito> items = itemCarritoRepository.findByCarrito(carritoActual);
        
        // --- NUEVA LÓGICA DE STOCK ---
        for (itemCarrito item : items) {
            Producto producto = item.getProducto();
            int cantidadComprada = item.getCantidad();
            
            if (producto.getStock() < cantidadComprada) {
                throw new RuntimeException("Lo sentimos, no hay suficiente stock de: " + producto.getNombre());
            }
            
            // Restamos la cantidad y guardamos el nuevo stock
            producto.setStock(producto.getStock() - cantidadComprada);
            productoRepository.save(producto);
        }
        // ------------------------------

        // El resto del código sigue igual...
        Double totalVenta = items.stream()
                .mapToDouble(i -> i.getSubTotal() * i.getCantidad())
                .sum();

        carritoActual.setEstado("COMPRADO");
        carritoRepository.save(carritoActual);

        pedidoService.crearPedido(carritoActual.getIdCarrito(), datosEnvio, totalVenta);

        // Relevo de carrito...
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setEstado("PENDIENTE");
        Carrito guardado = carritoRepository.save(nuevoCarrito);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdCarrito(guardado.getIdCarrito());
        
        return dto;
    }

    @Override
    public UsuarioDTO login(String correo, String password) {
        Optional<Usuario> userOpt = usuarioRepository.findByCorreoAndPassword(correo, password);

        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            UsuarioDTO dto = new UsuarioDTO();
            dto.setIdUsuario(user.getIdUsuario());
            dto.setNombre(user.getNombre());
            dto.setCorreo(user.getCorreo());
            dto.setRol(user.getRol());

            // Al loguear, buscamos si tiene un carrito pendiente o creamos uno
            Carrito carrito = carritoRepository.findByUsuarioAndEstado(user, "PENDIENTE")
                    .orElseGet(() -> {
                        Carrito nuevo = new Carrito();
                        nuevo.setUsuario(user);
                        nuevo.setEstado("PENDIENTE");
                        return carritoRepository.save(nuevo);
                    });

            dto.setIdCarrito(carrito.getIdCarrito());
            return dto;
        }
        return null;
    }
    public List<Carrito> listarVentasRealizadas() {
        return carritoRepository.findByEstadoOrderByEstadoDesc("COMPRADO");
    }

    @Override
    @Transactional
    public UsuarioDTO registrarNuevoUsuario(Usuario usuario) {
        // 1. Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 2. Asignar rol por defecto
        usuario.setRol("CLIENTE");

        // 3. Guardar el usuario para generar su ID
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 4. CREAR EL CARRITO INICIAL (Vital para Cosarbo)
        Carrito carritoInicial = new Carrito();
        carritoInicial.setUsuario(usuarioGuardado);
        carritoInicial.setEstado("PENDIENTE");
        Carrito carritoGuardado = carritoRepository.save(carritoInicial);

        // 5. Retornar el DTO para el Frontend
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuarioGuardado.getIdUsuario());
        dto.setNombre(usuarioGuardado.getNombre());
        dto.setRol(usuarioGuardado.getRol());
        dto.setIdCarrito(carritoGuardado.getIdCarrito());

        return dto;
    }
}