import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { Container, Row, Col, Table, Button, Card, Image, Alert, Spinner } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';

const Carrito = () => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const cartId = localStorage.getItem('cart_id');
    const navigate = useNavigate();

    // 1. Cargar los items del carrito desde el backend
    const fetchItems = async () => {
        try {
            const res = await api.get(`/api/v1/items-carrito/carrito/${cartId}`);
            setItems(res.data);
            setLoading(false);
        } catch (err) {
            console.error("Error al obtener items:", err);
            setError("No pudimos conectar con el servidor de Cosarbo.");
            setLoading(false);
        }
    };

    useEffect(() => {
        if (cartId) {
            fetchItems();
        } else {
            setLoading(false);
        }
    }, [cartId]);

    // 2. Modificar cantidad (+ o -)
    const handleCantidad = async (idItem, nuevaCantidad) => {
        if (nuevaCantidad < 1) return;
        try {
            await api.put(`/api/v1/items-carrito/${idItem}/cantidad?nuevaCantidad=${nuevaCantidad}`);
            fetchItems(); // Recarga para actualizar subtotales y totales
        } catch (err) {
            console.error("Error al actualizar cantidad:", err);
        }
    };

    // 3. Eliminar un item del carrito
    const eliminarItem = async (idItem) => {
        if (!window.confirm("¿Deseas quitar este amigurumi del carrito?")) return;
        try {
            await api.delete(`/api/v1/items-carrito/${idItem}`);
            fetchItems();
        } catch (err) {
            console.error("Error al eliminar el producto:", err);
        }
    };

    // 4. Cálculos de totales
    const calcularSubtotal = () => items.reduce((acc, item) => acc + (item.subTotal * item.cantidad), 0);

    // 5. Navegar al Checkout
    const irAlCheckout = () => {
        navigate('/checkout');
    };

    if (loading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" variant="success" />
                <p className="mt-3 text-success">Revisando tu canasto de lanas...</p>
            </Container>
        );
    }

    if (items.length === 0) {
        return (
            <Container className="text-center mt-5 p-5 bg-light rounded shadow-sm">
                <i className="bi bi-cart-dash display-1 text-muted"></i>
                <h3 className="mt-3 fw-bold">Tu carrito está vacío</h3>
                <p className="text-muted">Parece que aún no has elegido ningún compañero tejido.</p>
                <Button as={Link} to="/catalogo" variant="success" className="rounded-pill px-4 mt-2">
                    Volver al Catálogo
                </Button>
            </Container>
        );
    }

    return (
        <Container className="mt-5 mb-5">
            <h2 className="fw-bold text-success mb-4">
                <i className="bi bi-bag-heart me-2"></i>Tu Carrito de Ternura
            </h2>
            
            {error && <Alert variant="danger">{error}</Alert>}

            <Row>
                {/* LISTADO DE PRODUCTOS */}
                <Col lg={8}>
                    <Card className="border-0 shadow-sm p-3 mb-4">
                        <Table responsive hover className="align-middle">
                            <thead className="bg-light text-muted small">
                                <tr>
                                    <th>Producto</th>
                                    <th>Precio</th>
                                    <th className="text-center">Cantidad</th>
                                    <th>Subtotal</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {items.map(item => (
                                    <tr key={item.idItem}>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <Image 
                                                    src={item.producto.imagenUrl || 'https://placehold.co/60x60?text=Cosarbo'} 
                                                    rounded 
                                                    style={{ width: '60px', height: '60px', objectFit: 'cover' }}
                                                    className="me-3 border"
                                                />
                                                <div>
                                                    <div className="fw-bold text-dark">{item.producto.nombre}</div>
                                                    <small className="text-muted">Amigurumi hecho a mano</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>${item.subTotal?.toLocaleString('es-CL')}</td>
                                        <td>
                                            <div className="d-flex justify-content-center align-items-center">
                                                <Button 
                                                    variant="outline-success" 
                                                    size="sm" 
                                                    className="rounded-circle py-0 px-2"
                                                    onClick={() => handleCantidad(item.idItem, item.cantidad - 1)}
                                                > - </Button>
                                                <span className="mx-3 fw-bold">{item.cantidad}</span>
                                                <Button 
                                                    variant="outline-success" 
                                                    size="sm" 
                                                    className="rounded-circle py-0 px-1"
                                                    onClick={() => handleCantidad(item.idItem, item.cantidad + 1)}
                                                > + </Button>
                                            </div>
                                        </td>
                                        <td className="fw-bold text-success">
                                            ${(item.subTotal * item.cantidad).toLocaleString('es-CL')}
                                        </td>
                                        <td>
                                            <Button 
                                                variant="link" 
                                                className="text-danger p-0"
                                                onClick={() => eliminarItem(item.idItem)}
                                            >
                                                <i className="bi bi-trash-fill fs-5"></i>
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </Card>
                    <Link to="/catalogo" className="text-success text-decoration-none fw-bold">
                        <i className="bi bi-arrow-left-short me-1"></i> Seguir eligiendo amigurumis
                    </Link>
                </Col>

                {/* RESUMEN Y BOTÓN DE CHECKOUT */}
                <Col lg={4}>
                    <Card className="border-0 shadow-sm p-4 bg-light">
                        <h4 className="fw-bold mb-4">Resumen de Compra</h4>
                        <div className="d-flex justify-content-between mb-2">
                            <span className="text-muted">Subtotal:</span>
                            <span className="fw-bold">${calcularSubtotal().toLocaleString('es-CL')}</span>
                        </div>
                        <hr />
                        <div className="d-flex justify-content-between mb-4">
                            <span className="h5 fw-bold">Total a pagar:</span>
                            <span className="h4 fw-bold text-success">
                                ${(calcularSubtotal()).toLocaleString('es-CL')}
                            </span>
                        </div>
                        <Button 
                            variant="success" 
                            size="lg" 
                            className="w-100 rounded-pill shadow-sm fw-bold mb-3"
                            onClick={irAlCheckout}
                        >
                            Ir al Checkout
                        </Button>
                        <div className="text-center">
                            <small className="text-muted">
                                <i className="bi bi-lock-fill me-1"></i> Compra segura en Cosarbo
                            </small>
                        </div>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Carrito;