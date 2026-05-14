import api from './apiConfig';

const productoService = {
  // GET -> @GetMapping listarTodos()
  obtenerTodos: async () => {
    const response = await api.get('/productos');
    return response.data;
  },
  // POST -> @PostMapping guardar(@RequestBody Producto producto)
  crear: async (productoData) => {
    const response = await api.post('/productos', productoData);
    return response.data;
  },
  // DELETE -> @DeleteMapping("/{id}")
  eliminar: async (id) => {
    await api.delete(`/productos/${id}`);
  }
};

export default productoService;