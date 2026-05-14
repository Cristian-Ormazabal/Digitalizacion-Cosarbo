package com.cosarbo.digitalizacion.controllers;

import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    // OBTENER TODOS LOS PRODUCTOS: El que llama Catalogo.jsx
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    // OBTENER DETALLE DE UN PRODUCTO
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Integer id) {
        try {
            Producto producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // BUSCAR PRODUCTOS (Opcional, para una barra de búsqueda)
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }
    @PostMapping
        public ResponseEntity<Producto> guardarProducto(@RequestBody Producto nuevoProducto) {
            try {
                // Guardamos el amigurumi en la BDD
                Producto guardado = productoRepository.save(nuevoProducto);
                return ResponseEntity.ok(guardado);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto datosActualizados) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(datosActualizados.getNombre());
                    p.setPrecio(datosActualizados.getPrecio());
                    p.setStock(datosActualizados.getStock());
                    // p.setImagen(datosActualizados.getImagen());
                    // p.setDescripcion(datosActualizados.getDescripcion());
                    return ResponseEntity.ok(productoRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}