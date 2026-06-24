import React, { useState, useEffect } from 'react';
import api from '../../api/apiConfig';
import { useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Badge, Form, Alert } from 'react-bootstrap';

export default function Perfil() {
    const navigate = useNavigate();

    const userId = localStorage.getItem('userId');
    const cartId = localStorage.getItem('cartId');
    const token = localStorage.getItem('token');

    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const [formData, setFormData] = useState({
        nombre: localStorage.getItem('user_name') || '',
        correo: localStorage.getItem('user_correo') || '',
        password: '',
        confirmarPassword: ''
    });

    const rol = localStorage.getItem('user_role') || 'CLIENTE';

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleGuardarCambios = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (formData.password && formData.password !== formData.confirmarPassword) {
            setError('Las contraseñas no coinciden.');
            return;
        }

        try {
            const payload = {
                nombre: formData.nombre,
                correo: formData.correo
            };
            
            if (formData.password) {
                payload.password = formData.password;
            }

            await api.put(`/api/v1/usuarios/${userId}`, payload);

            localStorage.setItem('user_name', formData.nombre);
            localStorage.setItem('user_correo', formData.correo);

            setSuccess('¡Tu perfil ha sido actualizado con éxito! 🌸');
            setIsEditing(false);
            
            setFormData(prev => ({ ...prev, password: '', confirmarPassword: '' }));

        } catch (err) {
            console.error("Error al actualizar perfil:", err);
            setError(err.response?.data?.message || "No se pudieron guardar los cambios en el servidor.");
        }
    };

    const handleCerrarSesion = () => {
        if (window.confirm("¿Seguro que deseas cerrar tu sesión en Cosarbo? 🌸")) {
            localStorage.clear();
            alert("Sesión cerrada correctamente. ¡Vuelve pronto!");
            navigate('/login');
        }
    };

    return (
        <Container className="mt-5 mb-5">
            <Row className="justify-content-center">
                <Col md={8} lg={6}>
                    <Card className="border-0 shadow-lg overflow-hidden">
                        
                        {/* Cabecera decorativa */}
                        <div className="bg-success py-5 text-white text-center position-relative">
                            <div className="display-1 mb-2">🌸</div>
                            <h3 className="fw-bold mb-0 color-white">{localStorage.getItem('user_name')}</h3>
                            <Badge 
                                bg={rol === 'ADMIN' ? 'danger' : 'light'} 
                                text={rol === 'ADMIN' ? 'white' : 'success'}
                                className="position-absolute top-0 start-0 m-3 px-3 py-2 rounded-pill text-uppercase shadow-sm"
                            >
                                {rol}
                            </Badge>
                        </div>

                        <Card.Body className="p-4 bg-white">
                            
                            {/* Alertas de retroalimentación */}
                            {error && <Alert variant="danger" className="text-center py-2 small">{error}</Alert>}
                            {success && <Alert variant="success" className="text-center py-2 small">{success}</Alert>}

                            {/* MODO EDICIÓN FORMULARIO */}
                            {isEditing ? (
                                <Form onSubmit={handleGuardarCambios}>
                                    <h4 className="fw-bold text-success mb-4 border-bottom pb-2">
                                        <i className="bi bi-pencil-square me-2"></i>Modificar Datos
                                    </h4>
                                    
                                    <Form.Group className="mb-3">
                                        <Form.Label className="small fw-bold text-muted">Nombre Completo</Form.Label>
                                        <Form.Control 
                                            type="text"
                                            name="nombre"
                                            value={formData.nombre}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label className="small fw-bold text-muted">Correo Electrónico</Form.Label>
                                        <Form.Control 
                                            type="email"
                                            name="correo"
                                            value={formData.correo}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label className="small fw-bold text-muted">Nueva Contraseña (Opcional)</Form.Label>
                                        <Form.Control 
                                            type="password"
                                            name="password"
                                            placeholder="Dejar en blanco para mantener la actual"
                                            value={formData.password}
                                            onChange={handleInputChange}
                                        />
                                    </Form.Group>

                                    {formData.password && (
                                        <Form.Group className="mb-4">
                                            <Form.Label className="small fw-bold text-muted">Confirmar Nueva Contraseña</Form.Label>
                                            <Form.Control 
                                                type="password"
                                                name="confirmarPassword"
                                                placeholder="********"
                                                value={formData.confirmarPassword}
                                                onChange={handleInputChange}
                                                required
                                            />
                                        </Form.Group>
                                    )}

                                    <div className="d-flex gap-2">
                                        <Button type="submit" variant="success" className="w-100 rounded-pill fw-bold">
                                            Guardar Cambios
                                        </Button>
                                        <Button variant="outline-secondary" className="w-100 rounded-pill fw-bold" onClick={() => setIsEditing(false)}>
                                            Cancelar
                                        </Button>
                                    </div>
                                </Form>
                            ) : (
                                /* MODO LECTURA VISTA NORMAL */
                                <>
                                    <h4 className="fw-bold text-secondary mb-4 border-bottom pb-2">
                                        <i className="bi bi-person-vcard me-2 text-success"></i>Información de Cuenta
                                    </h4>

                                    <Row className="mb-3 text-start align-items-center">
                                        <Col xs={5} className="text-muted fw-semibold">Código de Usuario:</Col>
                                        <Col xs={7} className="fw-bold text-dark">#{userId || 'N/A'}</Col>
                                    </Row>

                                    <Row className="mb-3 text-start align-items-center">
                                        <Col xs={5} className="text-muted fw-semibold">Correo registrado:</Col>
                                        <Col xs={7} className="text-dark text-break">{formData.correo || 'No especificado'}</Col>
                                    </Row>

                                    <Row className="mb-3 text-start align-items-center">
                                        <Col xs={5} className="text-muted fw-semibold">Canasto Activo:</Col>
                                        <Col xs={7}>
                                            <Badge bg="success" className="px-2 py-1">
                                                ID {cartId || 'Ninguno'}
                                            </Badge>
                                        </Col>
                                    </Row>

                                    <Row className="mb-4 text-start align-items-center">
                                        <Col xs={5} className="text-muted fw-semibold">Estado de Conexión:</Col>
                                        <Col xs={7} className="text-success fw-bold small">
                                            <span className="spinner-grow spinner-grow-sm text-success me-2"></span>
                                            Sesión Protegida Activa
                                        </Col>
                                    </Row>

                                    <hr />

                                    <div className="d-grid gap-2 mt-4">
                                        <Button 
                                            variant="success" 
                                            className="rounded-pill fw-bold"
                                            onClick={() => setIsEditing(true)}
                                        >
                                            <i className="bi bi-pencil-square me-2"></i>Editar Datos de Perfil
                                        </Button>

                                        <Button 
                                            variant="outline-success" 
                                            className="rounded-pill fw-bold"
                                            onClick={() => navigate('/pedidos')}
                                        >
                                            <i className="bi bi-bag-heart me-2"></i>Ver Mis Pedidos
                                        </Button>
                                        
                                        <Button 
                                            variant="danger" 
                                            className="rounded-pill fw-bold mt-2 shadow-sm"
                                            onClick={handleCerrarSesion}
                                        >
                                            <i className="bi bi-box-arrow-right me-2"></i>Cerrar Sesión
                                        </Button>
                                    </div>
                                </>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}