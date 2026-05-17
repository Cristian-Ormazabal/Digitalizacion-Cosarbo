package com.cosarbo.digitalizacion.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cosarbo.digitalizacion.entities.Producto;
import com.cosarbo.digitalizacion.repositories.ProductoRepository;
import com.cosarbo.digitalizacion.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = {"http://localhost:5173", "https://cosarbo.netlify.app"})
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private Cloudinary cloudinary;

    // OBTENER TODOS LOS PRODUCTOS
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

    // BUSCAR PRODUCTOS 
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // CREAR PRODUCTO: Adaptado para procesar imágenes locales y subirlas a Cloudinary
    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {
        try {
            // >>> RASTREADORES EN CONSOLA DE JAVA <<< (DEBUG)
            System.out.println("📥 ENDPOINT /crear INVOCADO CON ÉXITO");
            System.out.println("📦 Datos recibidos -> Nombre: " + nombre + " | Precio: " + precio + " | Stock: " + stock);
            
            if (foto == null) {
                System.out.println("⚠️ ALERTA: El parámetro 'foto' llegó completamente NULO (null). React no lo envió bien.");
            } else if (foto.isEmpty()) {
                System.out.println("⚠️ ALERTA: El parámetro 'foto' llegó, pero está VACÍO (0 bytes).");
            } else {
                System.out.println("🔥 ¡ÉXITO! Archivo binario recibido.");
                System.out.println("📁 Nombre original del archivo: " + foto.getOriginalFilename());
                System.out.println("⚖️ Tamaño del archivo: " + foto.getSize() + " bytes");
                System.out.println("📋 Tipo de contenido: " + foto.getContentType());
            }

            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setDescripcion(descripcion);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setStock(stock);

            if (foto != null && !foto.isEmpty()) {
                System.out.println("☁️ Intentando conectar con Cloudinary...");
                Map uploadResult = cloudinary.uploader().upload(foto.getBytes(), ObjectUtils.emptyMap());
                String urlCloudinary = (String) uploadResult.get("secure_url");
                System.out.println("🚀 URL generada por Cloudinary: " + urlCloudinary);
                
                nuevoProducto.setImagen(urlCloudinary); 
            } else {
                System.out.println("ℹ️ Asignando imagen por defecto (placeholder)...");
                nuevoProducto.setImagen("https://via.placeholder.com/150");
            }

            Producto guardado = productoService.guardar(nuevoProducto);
            System.out.println("💾 Producto guardado exitosamente en MySQL con ID: " + guardado.getIdProducto());
            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            System.out.println("❌ ERROR CRÍTICO EN EL CONTROLLER: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al procesar el producto: " + e.getMessage());
        }
    }
    // ACTUALIZAR PRODUCTO (PUT)
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProductoConFoto(
            @PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {
        try {
            // Se busca el amigurumi real que ya existe en la base de datos
            return productoRepository.findById(id)
                    .map(productoExistente -> {
                        try {
                            // Se actualizan los campos de texto
                            productoExistente.setNombre(nombre);
                            productoExistente.setDescripcion(descripcion);
                            productoExistente.setPrecio(precio);
                            productoExistente.setStock(stock);

                            // Se evalúa la foto: 
                            // Si el usuario subió una NUEVA foto, se reemplaza en Cloudinary
                            if (foto != null && !foto.isEmpty()) {
                                Map uploadResult = cloudinary.uploader().upload(foto.getBytes(), ObjectUtils.emptyMap());
                                String urlCloudinary = (String) uploadResult.get("secure_url");
                                productoExistente.setImagen(urlCloudinary); // Reemplaza la URL vieja
                            }
                            // Si 'foto' viene nulo, se mantiene la foto intacta ('productoExistente.getImagen()')

                            // Se guardan los cambios sobre la MISMA fila de la base de datos
                            Producto actualizado = productoService.guardar(productoExistente);
                            return ResponseEntity.ok(actualizado);

                        } catch (Exception e) {
                            throw new RuntimeException("Error al procesar la imagen en Cloudinary: " + e.getMessage());
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el producto: " + e.getMessage());
        }
    }

    // ELIMINAR UN PRODUCTO DEL INVENTARIO (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        try {
            // Verifica si existe el producto existe antes de borrar
            return productoRepository.findById(id)
                    .map(producto -> {
                        // Si existe, se borra físicamente de la BDD
                        productoRepository.delete(producto);
                        return ResponseEntity.ok().body("¡Producto eliminado exitosamente!");
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            // Captura errores por si el producto está amarrado a un carrito histórico (Restricción de FK)
            return ResponseEntity.internalServerError()
                    .body("No se puede eliminar el producto porque está asociado a un historial de compras: " + e.getMessage());
        }
    }
}