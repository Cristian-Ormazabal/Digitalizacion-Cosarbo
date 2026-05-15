import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { Carousel, Container, Row, Col, Card, Button, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

import costuras from '../../assets/costuras-home.jpg';
import amigurumis from '../../assets/amigurumi-home.jpg';

const Home = () => {

  const navigate = useNavigate();
  const [productos, setProductos] = React.useState([]);
  const [loading, setLoading] = React.useState(true);

  useEffect(() => {
    const obtenerProductosRecientes = async () => {
      try {
          const response = await api.get('/api/v1/productos');
          
          // REVISIÓN AQUÍ: Axios guarda la respuesta en .data
          // Usamos una copia [...response.data] porque .reverse() modifica el original
          const ultimosProductos = [...response.data]
              .reverse() 
              .slice(0, 4); 
          
          setProductos(ultimosProductos);
          setLoading(false);
      } catch (error) {
          console.error("Error al cargar recién llegados:", error);
          setLoading(false);
      }
  };

    obtenerProductosRecientes();
  }, []);

  const handleAgregarAlCarrito = (id) => {
    console.log("Agregar al carrito:", id);
  };

  return (
    <div style={{ backgroundColor: '#FEFAE0', minHeight: '100vh' }}>
      
      {/* 1. HERO CAROUSEL */}
      <Carousel fade interval={4000} pause="hover">
        <Carousel.Item>
          <img
            className="d-block w-100"
            src={amigurumis}
            alt="Amigurumis"
          />
          <Carousel.Caption className="custom-caption">
            <h1 className="display-4 fw-bold">Amigurumis con Alma</h1>
            <p>Diseños únicos tejidos 100% a mano para regalar amor.</p>
            <Button variant="warning" className="rounded-pill px-4" onClick={() => navigate('/catalogo')}>
              Ver Colección
            </Button>
          </Carousel.Caption>
        </Carousel.Item>

        <Carousel.Item>
          <img
            className="d-block w-100"
            src={costuras}
            alt="Servicios de costura"
          />
          <Carousel.Caption className="custom-caption">
            <h1 className="display-4 fw-bold">Costura y Reparaciones</h1>
            <p>Le damos una segunda vida a tus prendas favoritas con ajustes perfectos.</p>
            <Button variant="success" className="rounded-pill px-4" onClick={() => navigate('/servicios')}>
              Ver Servicios
            </Button>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>

      {/* COSTURAS */}
      <Container className="my-5">
        <div className="p-5 text-white rounded-5 shadow-lg" style={{ backgroundColor: '#283618' }}>
          <Row className="align-items-center">
            <Col md={8}>
              <h2 className="text-white">¿Tu prenda favorita necesita un ajuste?</h2>
              <p className="lead">Especialistas en bastas, cambios de cierre y ajustes de medida con retiro en local.</p>
            </Col>
            <Col md={4} className="text-md-end">
              <Button variant="light" size="lg" className="rounded-pill px-4 text-success fw-bold" onClick={() => navigate('/servicios')}>
                Ver Disponibilidad
              </Button>
            </Col>
          </Row>
        </div>
      </Container>

      {/* 2. CATEGORÍAS */}
      <Container className="my-5 text-center">
        <Row>
          <Col md={4}>
            <Button onClick={() => navigate('/catalogo')} className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#CCD5AE', borderRadius: '50%' }}></Button>
            <h4>Amigurumis</h4>
          </Col>
          <Col md={4}>
            <Button onClick={() => navigate('/servicios')} className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#D4A373', borderRadius: '50%' }}></Button>
            <h4>Costura</h4>
          </Col>
          <Col md={4}>
            <Button onClick={() => navigate('/especiales')} className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#E9EDC6', borderRadius: '50%' }}></Button>
            <h4>Especiales</h4>
          </Col>
        </Row>
      </Container>

      {/* 3. PRODUCTOS RECIENTES */}
      <Container className="my-5">
            <h2 className="text-center mb-4 fw-bold">Recién llegados 🧶</h2>
            
            {loading ? (
                <p className="text-center">Buscando amigurumis nuevos...</p>
            ) : (
                <Row>
                    {productos.map((prod) => (
                        <Col key={prod.idProducto} sm={12} md={6} lg={3} className="mb-4">
                            <Card className="h-100 border-0 shadow-sm card-hover">
                                <Card.Img 
                                    variant="top" 
                                    src={prod.imagen || 'https://via.placeholder.com/200'} 
                                    style={{ height: '250px', objectFit: 'cover' }}
                                />
                                <Card.Body className="d-flex flex-column">
                                    <Card.Title className="fw-bold">{prod.nombre}</Card.Title>
                                    <Card.Text className="text-muted flex-grow-1">
                                        {prod.descripcion?.substring(0, 50)}...
                                    </Card.Text>
                                    <div className="d-flex justify-content-between align-items-center mt-auto">
                                        <span className="fw-bold text-success">
                                            ${prod.precio?.toLocaleString('es-CL')}
                                        </span>
                                        {prod.stock === 0 && <Badge bg="danger">Agotado</Badge>}
                                    </div>
                                    <Button 
                                        variant="dark" 
                                        className="mt-3 w-100"
                                        disabled={prod.stock === 0}
                                        onClick={() => navigate('/producto/' + prod.idProducto)}
                                    >
                                        Ver Detalles
                                    </Button>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </Container>

    </div>
  );
};

export default Home;