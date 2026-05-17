import api from './apiConfig';

const authService = {

    login: async (credenciales) => {
        const response = await api.post('/usuarios/login', {
            email: credenciales.correo,   
            password: credenciales.password 
        });
        return response.data;
    },

    registro: async (datosUsuario) => {
        const payload = {
            nombre: datosUsuario.nombre,
            correo: datosUsuario.correo,
            password: datosUsuario.password,
            role: "CLIENTE" 
        };

        // Llamada al método crear de UsuarioController
        const response = await api.post('/usuarios', payload);
        return response.data;
    }
};

export default authService;