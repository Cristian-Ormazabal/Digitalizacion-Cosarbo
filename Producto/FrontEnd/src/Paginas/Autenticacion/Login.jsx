import React, { useState } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { useNavigate, Link } from 'react-router-dom';

const Login = () => {
    const [formData, setFormData] = useState({
        correo: '',
        password: ''
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');

        try {
            // Llamada al endpoint v1 que definimos en el UsuarioController
            const response = await api.post('/api/v1/usuarios/login', formData);
            
            // response.data contiene nuestro UsuarioDTO
            const user = response.data;

            // PERSISTENCIA DE DATOS EN COSARBO
            // Guardamos el idUsuario y el idCarrito que viene del relevo automático
            localStorage.setItem('user_id', user.idUsuario);
            localStorage.setItem('cart_id', user.idCarrito);
            localStorage.setItem('user_name', user.nombre);
            localStorage.setItem('user_role', user.rol);

            console.log("Login exitoso. Carrito asignado:", user.idCarrito);

            alert(`¡Bienvenido, ${user.nombre}! Has iniciado sesión correctamente.`);
            
            // Redirección al catálogo
            if (user.rol === 'cliente') {
                navigate('/catalogo');
            } else {
                navigate('/admin');
            }
        } catch (err) {
            // Si el backend envía 401 (Unauthorized), capturamos el mensaje específico
            const mensajeError = err.response?.data || "Error al conectar con el servidor";
            setError(mensajeError);
            console.error("Error en login:", err);
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-5">
                    <div className="card shadow-lg border-0">
                        <div className="card-body p-5">
                            <h2 className="text-center mb-4 fw-bold">Iniciar Sesión</h2>
                            <p className="text-center text-muted mb-4">Bienvenido a Cosarbo</p>

                            {error && (
                                <div className="alert alert-danger py-2 text-center" role="alert">
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleLogin}>
                                <div className="mb-3">
                                    <label className="form-label">Correo Electrónico</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        name="correo"
                                        placeholder="ejemplo@correo.com"
                                        value={formData.correo}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <div className="mb-4">
                                    <label className="form-label">Contraseña</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        name="password"
                                        placeholder="********"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <button type="submit" className="btn btn-primary w-100 py-2 fw-bold shadow-sm">
                                    Ingresar
                                </button>
                            </form>

                            <div className="text-center mt-4">
                                <span className="text-muted">¿No tienes cuenta? </span>
                                <Link to="/registro" className="text-decoration-none fw-bold">Regístrate aquí</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;