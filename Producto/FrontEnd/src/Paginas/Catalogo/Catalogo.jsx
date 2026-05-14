import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { useNavigate } from 'react-router-dom'; // Importante para la navegación
import { Container, Row, Col, Card, Button, Badge, Spinner, Alert } from 'react-bootstrap';

const Catalogo = () => {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // Hook para redirigir

    // 1. Carga de productos desde el Backend
    useEffect(() => {
        const fetchProductos = async () => {
            try {
                const response = await api.get('/api/v1/productos');
                setProductos(response.data);
                setLoading(false);
            } catch (err) {
                console.error("Error al cargar productos:", err);
                setError("No se pudo conectar con el servidor de Cosarbo.");
                setLoading(false);
            }
        };
        fetchProductos();
    }, []);

    // 2. Función para añadir al carrito
    const agregarAlCarrito = async (producto) => {
        const cartId = localStorage.getItem('cart_id');
        
        if (!cartId) {
            alert("¡Hola! Debes iniciar sesión para añadir amigurumis al carrito.");
            navigate('/login');
            return;
        }

        try {
            const itemNuevo = {
                idCarrito: parseInt(cartId),
                idProducto: producto.idProducto,
                cantidad: 1,
                subTotal: producto.precio
            };

            const response = await api.post('/api/v1/items-carrito', itemNuevo);
            
            if (response.status === 200 || response.status === 201) {
                alert(`¡${producto.nombre} añadido con éxito!`);
            }

        } catch (err) {
            console.error("Error al añadir:", err.response?.data);
            alert("Error al añadir: " + (err.response?.data?.message || "Revisa el stock disponible"));
        }
    };

    if (loading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" variant="success" />
                <p className="mt-3 text-success fw-bold">Cargando catálogo artesanal...</p>
            </Container>
        );
    }

    return (
        <Container className="mt-5 mb-5">
            <div className="text-center mb-5">
                <h1 className="fw-bold text-success display-4">Cosarbo Amigurumis</h1>
                <p className="text-muted">Hechos a mano con cariño en Maipú</p>
                <div className="mx-auto bg-success" style={{ height: '3px', width: '50px' }}></div>
            </div>

            {error && <Alert variant="danger" className="text-center">{error}</Alert>}

            <Row>
                {productos.map((prod) => (
                    <Col key={prod.idProducto} xs={12} md={6} lg={4} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm card-hover">
                            <div className="position-relative">
                                {/* OJO: Usamos prod.imagen que es el nombre que usamos en el Admin Panel */}
                                <Card.Img 
                                    variant="top" 
                                    src={prod.imagen || 'https://placehold.co/400x400?text=Cosarbo+Amigurumi'} 
                                    alt={prod.nombre}
                                    style={{ height: '280px', objectFit: 'cover', cursor: 'pointer' }}
                                    onClick={() => navigate(`/producto/${prod.idProducto}`)} // Clic en imagen también lleva al detalle
                                />
                                {prod.stock <= 0 && (
                                    <Badge bg="dark" className="position-absolute top-0 start-0 m-2">Agotado</Badge>
                                )}
                            </div>
                            
                            <Card.Body className="d-flex flex-column p-4">
                                <Card.Title className="fw-bold h4">{prod.nombre}</Card.Title>
                                <Card.Text className="text-muted flex-grow-1">
                                    {prod.descripcion?.substring(0, 100)}...
                                </Card.Text>
                                
                                <div className="d-flex justify-content-between align-items-center mt-3 mb-3">
                                    <span className="fs-3 fw-bold text-success">
                                        ${prod.precio?.toLocaleString('es-CL')}
                                    </span>
                                    <Badge bg={prod.stock > 0 ? "light" : "danger"} text={prod.stock > 0 ? "dark" : "white"}>
                                        {prod.stock > 0 ? `${prod.stock} disponibles` : 'Sin stock'}
                                    </Badge>
                                </div>

                                <div className="d-grid gap-2">
                                    <Button 
                                        variant="success" 
                                        className="rounded-pill fw-bold"
                                        onClick={() => agregarAlCarrito(prod)}
                                        disabled={prod.stock <= 0}
                                    >
                                        {prod.stock <= 0 ? 'Agotado' : 'Agregar al Carrito'}
                                    </Button>
                                    
                                    {/* BOTÓN "VER MÁS" AGREGADO */}
                                    <Button 
                                        variant="outline-secondary" 
                                        className="rounded-pill btn-sm"
                                        onClick={() => navigate(`/producto/${prod.idProducto}`)}
                                    >
                                        Ver detalles
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default Catalogo;