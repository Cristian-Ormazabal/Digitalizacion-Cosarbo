import api from './apiConfig';

const productoService = {
  obtenerTodos: async () => {
    const response = await api.get('/productos');
    return response.data;
  },
  crear: async (productoData) => {
    const response = await api.post('/productos', productoData);
    return response.data;
  },
  eliminar: async (id) => {
    await api.delete(`/productos/${id}`);
  }
};

export default productoService;