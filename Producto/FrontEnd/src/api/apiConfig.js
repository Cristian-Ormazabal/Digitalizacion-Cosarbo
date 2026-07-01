import axios from 'axios';

const api = axios.create({
    // Si la URL del navegador es localhost, apunta al puerto 8080 local. 
    // Si no, apunta de inmediato al backend alojado en Render/Railway.
    baseURL: window.location.hostname === 'localhost' 
        ? 'http://localhost:8080' 
        : import.meta.env.VITE_API_URL

});

// Interceptor de peticiones (Mantiene el token inyectado)
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default api;