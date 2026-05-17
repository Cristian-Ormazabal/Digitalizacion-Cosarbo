import React, { useState } from 'react';
import axios from 'axios';
// import api from '../../apiConfig';
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
            const response = await axios.post('http://localhost:8080/api/v1/usuarios/login', formData);
            const user = response.data; 
            // Se guardan los datos de identidad iniciales
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('userId', response.data.idUsuario); 
            localStorage.setItem('user_correo', response.data.correo);
            localStorage.setItem('user_name', response.data.nombre);
            localStorage.setItem('user_role', response.data.rol);

            if (user.rol === 'CLIENTE') {
                try {
                    const resCarrito = await axios.post(
                        `http://localhost:8080/api/v1/carrito/usuario/${user.idUsuario}`, 
                        {}, 
                        { headers: { Authorization: `Bearer ${user.token}` } }
                    );
                    
                    localStorage.setItem('cartId', resCarrito.data.idCarrito);
                    console.log("Carrito asegurado y sincronizado con éxito ID:", resCarrito.data.idCarrito);
                } catch (carritoErr) {
                    console.error("Error preventivo al inicializar el carrito del usuario:", carritoErr);
                    if (user.idCarrito) {
                        localStorage.setItem('cartId', user.idCarrito);
                    }
                }
            }

            alert(`¡Bienvenido, ${user.nombre}! Has iniciado sesión correctamente. 🌸`);
            
            // Redirección limpia según el rol
            if (user.rol === 'ADMIN' || user.rol === 'ROLE_ADMIN') {
                navigate('/admin');
            } else {
                navigate('/catalogo');
            }

        } catch (err) {
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