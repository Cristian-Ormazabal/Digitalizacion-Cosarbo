import React, { useState, useEffect } from 'react';
import api from '../../api/apiConfig';
import { Container, Row, Col, Table, Button, Card, Image, Alert, Spinner } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';

const Carrito = () => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const cartId = localStorage.getItem('cartId'); 
    const navigate = useNavigate();

    const fetchItems = async () => {
        try {
            if (!cartId || cartId === "undefined") {
                setLoading(false);
                return;
            }
            const res = await api.get(`/api/v1/items-carrito/carrito/${cartId}`);
            setItems(Array.isArray(res.data) ? res.data : []);
            setLoading(false);
        } catch (err) {
            console.error("Error al obtener items:", err);
            setError("No pudimos conectar con el servidor de Cosarbo.");
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchItems();
    }, [cartId]);

    const handleCantidad = async (idItem, nuevaCantidad) => {
        if (nuevaCantidad < 1) return;
        try {
            await api.put(`/api/v1/items-carrito/${idItem}/cantidad?nuevaCantidad=${nuevaCantidad}`);
            fetchItems(); 
        } catch (err) {
            console.error("Error al actualizar cantidad:", err);
        }
    };

    const eliminarItem = async (idItem) => {
        if (!window.confirm("¿Deseas quitar este amigurumi del carrito?")) return;
        try {
            await api.delete(`/api/v1/items-carrito/${idItem}`);
            fetchItems(); // Refresca la lista
        } catch (err) {
            console.error("Error al eliminar el producto:", err);
        }
    };

    const calcularTotalGeneral = () => {
        return items.reduce((acc, item) => acc + (item.subTotal || 0), 0);
    };

    // 5. Navegar al Checkout
    const irAlCheckout = () => {
        navigate('/checkout');
    };

    if (loading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" variant="success" />
                <p className="mt-3 text-success">Revisando tu canasto de lanas... 🧶</p>
            </Container>
        );
    }

    if (!cartId || items.length === 0) {
        return (
            <Container className="text-center mt-5 p-5 bg-light rounded shadow-sm">
                <h2 className="mt-3 fw-bold">Tu carrito está vacío 🧶</h2>
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
                <i className="bi bi-bag-heart me-2"></i>Tu Carrito de Ternura 🌸
            </h2>
            
            {error && <Alert variant="danger">{error}</Alert>}

            <Row>
                {/* LISTADO DE PRODUCTOS EN LA CANASTA */}
                <Col lg={8}>
                    <Card className="border-0 shadow-sm p-3 mb-4">
                        <Table responsive hover className="align-middle">
                            <thead className="table-light text-muted small text-uppercase">
                                <tr>
                                    <th>Producto</th>
                                    <th>Precio Unitario</th>
                                    <th className="text-center">Cantidad</th>
                                    <th className="text-end">Subtotal</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {items.map(item => (
                                    <tr key={item.idItem}>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <Image 
                                                    src={item.producto?.imagen || 'https://via.placeholder.com/60'} 
                                                    rounded 
                                                    style={{ width: '60px', height: '60px', objectFit: 'cover' }}
                                                    className="me-3 border shadow-sm"
                                                />
                                                <div>
                                                    <div className="fw-bold text-dark">{item.producto?.nombre}</div>
                                                    <small className="text-muted">Hecho a mano con amor</small>
                                                </div>
                                            </div>
                                        </td>
                                        {/* Precio unitario desde la entidad Producto */}
                                        <td>${item.producto?.precio?.toLocaleString('es-CL')}</td>
                                        <td>
                                            <div className="d-flex justify-content-center align-items-center">
                                                <Button 
                                                    variant="outline-success" 
                                                    size="sm" 
                                                    className="rounded-circle py-0 px-2 fw-bold"
                                                    onClick={() => handleCantidad(item.idItem, item.cantidad - 1)}
                                                > - </Button>
                                                <span className="mx-3 fw-bold">{item.cantidad}</span>
                                                <Button 
                                                    variant="outline-success" 
                                                    size="sm" 
                                                    className="rounded-circle py-0 px-1 fw-bold"
                                                    onClick={() => handleCantidad(item.idItem, item.cantidad + 1)}
                                                > + </Button>
                                            </div>
                                        </td>
                                        {/* Subtotal calculado de forma segura por itemCarritoServiceImpl */}
                                        <td className="fw-bold text-success text-end">
                                            ${item.subTotal?.toLocaleString('es-CL')}
                                        </td>
                                        <td className="text-center">
                                            <Button 
                                                variant="link" 
                                                className="text-danger p-0 ms-2"
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
                        ← Seguir eligiendo amigurumis
                    </Link>
                </Col>

                {/* RESUMEN DE COBROS */}
                <Col lg={4}>
                    <Card className="border-0 shadow-sm p-4 bg-light">
                        <h4 className="fw-bold mb-4">Resumen de Compra</h4>
                        <div className="d-flex justify-content-between mb-2">
                            <span className="text-muted">Subtotal de ítems:</span>
                            <span className="fw-bold">${calcularTotalGeneral().toLocaleString('es-CL')}</span>
                        </div>
                        <hr />
                        <div className="d-flex justify-content-between mb-4">
                            <span className="h5 fw-bold">Total a pagar:</span>
                            <span className="h4 fw-bold text-success">
                                ${calcularTotalGeneral().toLocaleString('es-CL')}
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
                                🔒 Compra 100% segura en Cosarbo
                            </small>
                        </div>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Carrito;