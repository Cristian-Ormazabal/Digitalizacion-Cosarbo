import api from './apiConfig';

const authService = {

        // src/api/authService.js
    login: async (credenciales) => {
        // IMPORTANTE: Los nombres de las llaves deben ser 'email' y 'password'
        // tal como están en el LoginRequestDTO de Java.
        const response = await api.post('/usuarios/login', {
            email: credenciales.correo,   // Asumiendo que tu state se llama 'correo'
            password: credenciales.password // Asegúrate que aquí diga 'password'
        });
        return response.data;
    },

    registro: async (datosUsuario) => {
        // Estructura exacta que espera UsuarioRegistroDTO.java
        const payload = {
            nombre: datosUsuario.nombre,
            correo: datosUsuario.correo,
            password: datosUsuario.password,
            role: "CLIENTE" // Valor por defecto
        };

        // Llamada al método crear de UsuarioController
        const response = await api.post('/usuarios', payload);
        return response.data;
    }
};

export default authService;