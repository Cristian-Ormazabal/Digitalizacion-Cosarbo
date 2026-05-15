import api from './apiConfig';

const cartService = {
    crearCarrito: async (idUsuario) => {
        try {
            const payload = {
                usuario: { 
                    idUsuario: parseInt(idUsuario) 
                },
                estadoPedido: "PENDIENTE",
                total: 0
            };
            // Llamada al @PostMapping en CarritoController
            const response = await api.post('/carrito', payload);
            return response.data;
        } catch (error) {
            console.error("Error en crearCarrito:", error);
            throw error;
        }
    },

    guardarItem: async (idCarrito, item) => {
        const payload = {
            carrito: { idCarrito: Number(idCarrito) },
            producto: { idProducto: Number(item.idProducto) },
            cantidad: 1,
            subTotal: item.precio // El subtotal inicial es el precio x 1
        };
        const response = await api.post('/items', payload);
        return response.data;
    },

    // GET para recuperar el carrito activo del usuario
    obtenerPorUsuario: async (idUsuario) => {
        // Asumiendo que Tomas creó este endpoint en CarritoController
        const response = await api.get(`/carrito/usuario/${idUsuario}`);
        return response.data;
    },

    eliminarItem: async (idItem) => {
        await api.delete(`/items/${idItem}`);
    },

    // src/api/cartService.js

    finalizarCompra: async (idCarrito) => {
        const response = await api.post(`/carrito/${idCarrito}/finalizar`);
        return response.data;
    },
};

export default cartService;