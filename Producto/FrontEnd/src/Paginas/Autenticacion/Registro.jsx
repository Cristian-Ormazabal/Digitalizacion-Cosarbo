import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

const Registro = () => {
    const [formData, setFormData] = useState({
        nombre: '',
        correo: '',
        password: '',
        confirmarPassword: ''
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleRegistro = async (e) => {
        e.preventDefault();
        setError('');

        // Validación básica de contraseñas en el cliente
        if (formData.password !== formData.confirmarPassword) {
            setError('Las contraseñas no coinciden');
            return;
        }

        try {
            // Se envia los datos al endpoint /registrar que se configuró en el Controller
            await axios.post('http://localhost:8080/api/v1/usuarios/registrar', {
                nombre: formData.nombre,
                correo: formData.correo,
                password: formData.password
            });

            alert("¡Cuenta creada con éxito! 🌸 Por favor, inicia sesión con tus credenciales.");
            navigate('/login');

        } catch (error) {
            console.error("Error en registro:", error);
            setError(error.response?.data?.message || "Error al conectar con el servidor");
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card shadow-lg border-0">
                        <div className="card-body p-5">
                            <h2 className="text-center mb-4 fw-bold">Crear Cuenta</h2>
                            <p className="text-center text-muted mb-4">Únete a la comunidad de Cosarbo</p>

                            {error && (
                                <div className="alert alert-danger py-2 text-center" role="alert">
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleRegistro}>
                                <div className="mb-3">
                                    <label className="form-label">Nombre Completo</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        name="nombre"
                                        placeholder="Tu nombre"
                                        value={formData.nombre}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

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

                                <div className="row">
                                    <div className="col-md-6 mb-3">
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
                                    <div className="col-md-6 mb-4">
                                        <label className="form-label">Confirmar</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            name="confirmarPassword"
                                            placeholder="********"
                                            value={formData.confirmarPassword}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                </div>

                                <button type="submit" className="btn btn-success w-100 py-2 fw-bold shadow-sm">
                                    Registrarse
                                </button>
                            </form>

                            <div className="text-center mt-4">
                                <span className="text-muted">¿Ya tienes cuenta? </span>
                                <Link to="/login" className="text-decoration-none fw-bold">Inicia sesión</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Registro;