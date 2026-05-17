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
            subTotal: item.precio 
        };
        const response = await api.post('/items', payload);
        return response.data;
    },

    obtenerPorUsuario: async (idUsuario) => {
        const response = await api.get(`/carrito/usuario/${idUsuario}`);
        return response.data;
    },

    eliminarItem: async (idItem) => {
        await api.delete(`/items/${idItem}`);
    },


    finalizarCompra: async (idCarrito) => {
        const response = await api.post(`/carrito/${idCarrito}/finalizar`);
        return response.data;
    },
};

export default cartService;