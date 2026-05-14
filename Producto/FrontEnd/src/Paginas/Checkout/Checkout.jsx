import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { useNavigate } from 'react-router-dom';
import { Container, Row, Col, Form, Button, Table, Card, Spinner } from 'react-bootstrap';

const formatearPrecio = (precio) => {
    return new Intl.NumberFormat('es-CL', {
        style: 'currency',
        currency: 'CLP'
    }).format(precio);
};

export default function Checkout() {
    const [formData, setFormData] = useState({
        nombre: '',
        apellidos: '',
        correo: '',
        calle: '',
        departamento: '',
        region: 'Región Metropolitana de Santiago',
        comuna: 'Maipú',
        indicaciones: ''
    });

    const [itemsCarrito, setItemsCarrito] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    // Obtenemos los IDs de la sesión
    const cartId = localStorage.getItem('cart_id');
    const userId = localStorage.getItem('user_id');

    useEffect(() => {
        // Validación de seguridad: si no hay carrito o usuario, volver atrás
        if (!cartId || !userId) {
            alert("Sesión no válida o carrito vacío.");
            navigate('/catalogo');
            return;
        }

        const fetchCarrito = async () => {
            try {
                // Usamos tu endpoint de items-carrito filtrado por el carrito actual
                const response = await api.get(`/api/v1/items-carrito/carrito/${cartId}`);
                setItemsCarrito(response.data);
                setIsLoading(false);
            } catch (error) {
                console.error("Error al cargar el resumen del checkout:", error);
                setIsLoading(false);
            }
        };
        fetchCarrito();
    }, [cartId, userId, navigate]);

    const handleFormChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handlePagarAhora = async () => {
        // Validaciones básicas antes de enviar
        if (!formData.nombre || !formData.correo || !formData.calle) {
            alert('Por favor, completa los campos obligatorios (Nombre, Correo y Calle).');
            return;
        }

        try {
            // Enviamos el formData al backend para procesar la "compra"
            const response = await api.post(
                `/api/v1/usuarios/${userId}/finalizar-compra`,
                formData
            );

            // EL RELEVO: 
            // El backend nos devuelve el ID del NUEVO carrito vacío en 'idCarrito'
            const nuevoIdCarrito = response.data.idCarrito;
            
            // Actualizamos el localStorage para que el usuario empiece con carrito limpio
            localStorage.setItem('cart_id', nuevoIdCarrito);

            // Redirigimos a la página de éxito usando el ID del carrito que ACABAMOS DE PAGAR
            navigate(`/pagorealizado/${cartId}`);

        } catch (error) {
            console.error("Error al procesar el pago:", error);
            // Si falla, enviamos al usuario a la página de error con sus datos para que no los pierda
            navigate('/pagofallido', { state: { formData, items: itemsCarrito } });
        }
    };

    const totalCarrito = itemsCarrito.reduce((total, item) => 
        total + (item.subTotal * item.cantidad), 0
    );

    if (isLoading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" variant="success" />
                <p>Preparando tu orden de amigurumis...</p>
            </Container>
        );
    }

    return (
        <Container className="mt-5 mb-5">
            <h2 className="fw-bold text-success mb-4">Finalizar Compra</h2>
            <Row>
                {/* COLUMNA IZQUIERDA: FORMULARIO */}
                <Col lg={7}>
                    <Card className="p-4 border-0 shadow-sm mb-4">
                        <h4 className="mb-4">Datos de Despacho</h4>
                        <Form>
                            <Row>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Nombre*</Form.Label>
                                        <Form.Control 
                                            name="nombre" 
                                            placeholder="Tu nombre"
                                            value={formData.nombre} 
                                            onChange={handleFormChange} 
                                        />
                                    </Form.Group>
                                </Col>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Apellidos*</Form.Label>
                                        <Form.Control 
                                            name="apellidos" 
                                            placeholder="Tus apellidos"
                                            value={formData.apellidos} 
                                            onChange={handleFormChange} 
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Form.Group className="mb-3">
                                <Form.Label>Correo Electrónico*</Form.Label>
                                <Form.Control 
                                    type="email" 
                                    name="correo" 
                                    value={formData.correo} 
                                    onChange={handleFormChange} 
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Calle y Número*</Form.Label>
                                <Form.Control 
                                    name="calle" 
                                    placeholder="Ej: Av. Pajaritos 1234"
                                    value={formData.calle} 
                                    onChange={handleFormChange} 
                                />
                            </Form.Group>
                            <Row>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Comuna*</Form.Label>
                                        <Form.Select name="comuna" value={formData.comuna} onChange={handleFormChange}>
                                            <option value="Maipú">Maipú</option>
                                            <option value="Cerrillos">Cerrillos</option>
                                            <option value="Santiago">Santiago</option>
                                            <option value="Estación Central">Estación Central</option>
                                        </Form.Select>
                                    </Form.Group>
                                </Col>
                                <Col md={6}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Depto / Casa (Opcional)</Form.Label>
                                        <Form.Control 
                                            name="departamento" 
                                            value={formData.departamento} 
                                            onChange={handleFormChange} 
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                        </Form>
                    </Card>
                </Col>

                {/* COLUMNA DERECHA: RESUMEN */}
                <Col lg={5}>
                    <Card className="p-4 border-0 shadow-sm bg-light">
                        <h4 className="fw-bold mb-4">Resumen del Pedido</h4>
                        <Table responsive size="sm" className="mb-4">
                            <thead>
                                <tr>
                                    <th>Amigurumi</th>
                                    <th className="text-center">Cant.</th>
                                    <th className="text-end">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {itemsCarrito.map(item => (
                                    <tr key={item.idItem}>
                                        <td className="small">{item.producto.nombre}</td>
                                        <td className="text-center">{item.cantidad}</td>
                                        <td className="text-end">{formatearPrecio(item.subTotal * item.cantidad)}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                        
                        <div className="d-flex justify-content-between h5 fw-bold text-success border-top pt-3">
                            <span>Total a pagar:</span>
                            <span>{formatearPrecio(totalCarrito)}</span>
                        </div>

                        <Button 
                            variant="success" 
                            size="lg" 
                            className="w-100 mt-4 rounded-pill fw-bold shadow"
                            onClick={handlePagarAhora}
                        >
                            Confirmar y Pagar
                        </Button>
                        <p className="text-center text-muted small mt-3">
                            <i className="bi bi-shield-lock-fill"></i> Transacción segura para Cosarbo
                        </p>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}