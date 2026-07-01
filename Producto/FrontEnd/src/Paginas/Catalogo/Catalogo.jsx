import React, { useState, useEffect } from 'react';
import api from '../../api/apiConfig';
import { useNavigate } from 'react-router-dom'; 
import { Container, Row, Col, Card, Button, Badge, Spinner, Alert } from 'react-bootstrap';

const Catalogo = () => {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); 

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

    const handleAgregarCarrito = async (productoSeleccionado) => {
        try {
            const carritoId = localStorage.getItem("cartId"); 

            if (!carritoId || carritoId === "undefined") {
                alert("Por favor, inicia sesión para añadir amigurumis a tu carrito. 🧶");
                return;
            }

            const productoIdReal = parseInt(productoSeleccionado?.idProducto);

            if (!productoIdReal || isNaN(productoIdReal)) {
                alert("Error: No se pudo identificar el código de este amigurumi.");
                console.error("El idProducto se calculó como NaN.");
                return;
            }

            const payload = {
                idCarrito: parseInt(carritoId),  
                idProducto: productoIdReal, 
                cantidad: 1                      
            };

            console.log("✈️ Enviando amigurumi al carrito desde Catálogo... Payload:", payload);

            await api.post('/api/v1/carrito/agregar', payload);
            
            alert(`¡${productoSeleccionado.nombre} añadido al carrito con éxito! 🛒🌸`);
        } catch (error) {
            console.error("Error al añadir ítem al carrito:", error);
            alert(error.response?.data || "Hubo un problema al procesar la solicitud en Cosarbo.");
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
                <h3 className="text-muted">Hechos a mano con cariño en Maipú</h3>
                <div className="mx-auto bg-success" style={{ height: '3px', width: '50px' }}></div>
            </div>

            {error && <Alert variant="danger" className="text-center">{error}</Alert>}

            <Row>
                {productos.map((prod) => (
                    <Col key={prod.idProducto} xs={12} md={6} lg={3} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm card-hover">
                            <div className="position-relative">
                                <Card.Img 
                                    variant="top" 
                                    src={prod.imagen || 'https://placehold.co/400x400?text=Cosarbo+Amigurumi'} 
                                    alt={prod.nombre}
                                    style={{ height: '240px', objectFit: 'contain', backgroundColor: '#fefae0', cursor: 'pointer' }}
                                    onClick={() => navigate(`/producto/${prod.idProducto}`)} 
                                />
                                {prod.stock <= 0 && (
                                    <Badge bg="dark" className="position-absolute top-0 start-0 m-2">Agotado</Badge>
                                )}
                            </div>
                            
                            <Card.Body className="d-flex flex-column p-4">
                                <Card.Title className="fw-bold h4">{prod.nombre}</Card.Title>
                                <Card.Text className="text-muted flex-grow-1">
                                    {prod.descripcion?.substring(0, 100)}
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
                                        className="rounded-pill fw-bold fs-5"
                                        onClick={() => handleAgregarCarrito(prod)}
                                        disabled={prod.stock <= 0}
                                    >
                                        {prod.stock <= 0 ? 'Agotado' : 'Agregar al Carrito'}
                                    </Button>
                                    
                                    <Button 
                                        variant="outline-secondary" 
                                        className="rounded-pill btn-sm fs-5"
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