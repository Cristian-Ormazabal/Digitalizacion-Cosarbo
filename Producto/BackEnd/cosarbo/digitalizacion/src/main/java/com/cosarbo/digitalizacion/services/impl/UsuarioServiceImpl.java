package com.cosarbo.digitalizacion.services.impl;

import com.cosarbo.digitalizacion.entities.Usuario;
import com.cosarbo.digitalizacion.entities.itemCarrito;
import com.cosarbo.digitalizacion.entities.Carrito;
import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.dto.UsuarioActualizacionDTO;
import com.cosarbo.digitalizacion.dto.UsuarioDTO;
import com.cosarbo.digitalizacion.repositories.itemCarritoRepository;
import com.cosarbo.digitalizacion.repositories.UsuarioRepository;
import com.cosarbo.digitalizacion.repositories.CarritoRepository;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.PedidoService;
import com.cosarbo.digitalizacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORTANTE
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

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // Encriptar si se guarda o actualiza desde un flujo genérico
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
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
        
        for (itemCarrito item : items) {
            Producto producto = item.getProducto();
            int cantidadComprada = item.getCantidad();
            
            if (producto.getStock() < cantidadComprada) {
                throw new RuntimeException("Lo sentimos, no hay suficiente stock de: " + producto.getNombre());
            }
            
            producto.setStock(producto.getStock() - cantidadComprada);
            productoRepository.save(producto);
        }

        Double totalVenta = items.stream()
                .mapToDouble(i -> i.getSubTotal() * i.getCantidad())
                .sum();

        carritoActual.setEstado("COMPRADO");
        carritoRepository.save(carritoActual);

        pedidoService.crearPedido(carritoActual.getIdCarrito(), datosEnvio, totalVenta);

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
        // Se busca al usuario únicamente por correo electrónico
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(correo);

        // Si el usuario existe y la contraseña coincide usando BCrypt
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            Usuario user = userOpt.get();
            UsuarioDTO dto = new UsuarioDTO();
            dto.setIdUsuario(user.getIdUsuario());
            dto.setNombre(user.getNombre());
            dto.setCorreo(user.getCorreo());
            dto.setRol(user.getRol());

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
        return null; // Retorna null si el correo no existe o la contraseña no coincide
    }

    public List<Carrito> listarVentasRealizadas() {
        return carritoRepository.findByEstadoOrderByEstadoDesc("COMPRADO");
    }

    @Override
    @Transactional
    public UsuarioDTO registrarNuevoUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        usuario.setRol("CLIENTE");

        // Se encripta la contraseña en formato Hash antes de persistir en la BD
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Carrito carritoInicial = new Carrito();
        carritoInicial.setUsuario(usuarioGuardado);
        carritoInicial.setEstado("PENDIENTE");
        Carrito carritoGuardado = carritoRepository.save(carritoInicial);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuarioGuardado.getIdUsuario());
        dto.setNombre(usuarioGuardado.getNombre());
        dto.setRol(usuarioGuardado.getRol());
        dto.setCorreo(usuarioGuardado.getCorreo());
        dto.setIdCarrito(carritoGuardado.getIdCarrito());

        return dto;
    }

    @Override
    @Transactional
    public void actualizarUsuario(Integer idUsuario, UsuarioActualizacionDTO dto) {
        // Buscar que el usuario exista
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + idUsuario));

        // Actualizar campos básicos
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());

        // Validar si escribió una nueva contraseña
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Guardar cambios en la base de datos
        usuarioRepository.save(usuario);
    }
}