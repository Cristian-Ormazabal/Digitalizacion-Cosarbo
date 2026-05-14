import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { Container, Row, Col, Button, Badge, Spinner, Card } from 'react-bootstrap';

const formatearPrecio = (precio) => {
    return new Intl.NumberFormat('es-CL', {
        style: 'currency',
        currency: 'CLP'
    }).format(precio);
};

export default function DetallesProducto() {
    const { id } = useParams(); // Obtenemos el ID de la URL
    const navigate = useNavigate();
    const [producto, setProducto] = useState(null);
    const [loading, setLoading] = useState(true);
    const [agregando, setAgregando] = useState(false);

    useEffect(() => {
        const fetchProducto = async () => {
            try {
                // Endpoint para traer un solo producto por ID
                const response = await api.get(`/api/v1/productos/${id}`);
                setProducto(response.data);
                setLoading(false);
            } catch (error) {
                console.error("Error al cargar detalle:", error);
                setLoading(false);
            }
        };
        fetchProducto();
    }, [id]);

    const handleAgregarCarrito = async () => {
        const cartId = localStorage.getItem('cart_id');
        if (!cartId) {
            alert("Debes iniciar sesión para comprar");
            navigate('/login');
            return;
        }

        setAgregando(true);
        try {
            // Estructura para tu ItemCarrito (ajusta según tu backend)
            await api.post('/api/v1/items-carrito', {
                idCarrito: parseInt(cartId),
                idProducto: producto.idProducto,
                cantidad: 1,
                subTotal: producto.precio
            });
            alert(`${producto.nombre} se agregó a tu bolsa 🧶`);
            navigate('/catalogo');
        } catch (error) {
            console.error("Error al agregar:", error);
            alert("No pudimos agregar el producto");
        } finally {
            setAgregando(false);
        }
    };

    if (loading) return (
        <Container className="text-center mt-5">
            <Spinner animation="border" variant="success" />
        </Container>
    );

    if (!producto) return <Container className="mt-5 text-center"><h3>Producto no encontrado</h3></Container>;

    return (
        <Container className="mt-5 mb-5">
            <Row className="align-items-center">
                {/* IMAGEN */}
                <Col md={6} className="text-center mb-4 mb-md-0">
                    <Card className="border-0 shadow-sm overflow-hidden">
                        <Card.Img 
                            src={producto.imagen} 
                            alt={producto.nombre} 
                            style={{ objectFit: 'cover', maxHeight: '500px' }}
                        />
                    </Card>
                </Col>

                {/* INFO */}
                <Col md={6} className="ps-md-5">
                    <Badge bg="success" className="mb-2">Nuevo Lanzamiento</Badge>
                    <h1 className="display-4 fw-bold text-dark">{producto.nombre}</h1>
                    <h3 className="text-success fw-bold mb-4">
                        {formatearPrecio(producto.precio)}
                    </h3>
                    
                    <p className="lead text-muted mb-4">
                        {producto.descripcion}
                    </p>

                    <div className="mb-4">
                        <span className="fw-bold d-block mb-2">Disponibilidad:</span>
                        {producto.stock > 0 ? (
                            <Badge bg="info" className="p-2">
                                {producto.stock} unidades listas para envío en Maipú
                            </Badge>
                        ) : (
                            <Badge bg="danger" className="p-2">Agotado temporalmente</Badge>
                        )}
                    </div>

                    <div className="d-grid gap-2">
                        <Button 
                            variant="dark" 
                            size="lg" 
                            className="rounded-pill py-3 fw-bold shadow"
                            disabled={producto.stock === 0 || agregando}
                            onClick={handleAgregarCarrito}
                        >
                            {agregando ? 'Agregando...' : 'Agregar al Carrito'}
                        </Button>
                        <Button 
                            variant="outline-secondary" 
                            className="rounded-pill"
                            onClick={() => navigate('/catalogo')}
                        >
                            Volver al catálogo
                        </Button>
                    </div>

                    <p className="small text-center text-muted mt-3">
                        <i className="bi bi-truck me-2"></i> 
                        Envío gratis en compras sobre $30.000 (Solo Maipú)
                    </p>
                </Col>
            </Row>
        </Container>
    );
}