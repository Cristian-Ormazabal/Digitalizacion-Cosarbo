import React, { use } from 'react';
import { Carousel, Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

import costuras from '../../assets/costuras-home.jpg';
import amigurumis from '../../assets/amigurumi-home.jpg';

const Home = () => {

  const navigate = useNavigate();
  
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
            <div className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#CCD5AE', borderRadius: '50%' }}></div>
            <h4>Amigurumis</h4>
          </Col>
          <Col md={4}>
            <div className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#D4A373', borderRadius: '50%' }}></div>
            <h4>Costura</h4>
          </Col>
          <Col md={4}>
            <div className="category-circle mb-3 mx-auto shadow" style={{ width: '150px', height: '150px', backgroundColor: '#E9EDC6', borderRadius: '50%' }}></div>
            <h4>Especiales</h4>
          </Col>
        </Row>
      </Container>

      {/* 3. PRODUCTOS RECIENTES */}
      <Container className="pb-5">
        <h2 className="mb-4 text-success">Recién Llegados</h2>
        <Row>
          {[1, 2, 3, 4].map((item) => (
            <Col key={item} sm={6} md={3} className="mb-4">
              <Card className="h-100 border-0 shadow-sm card-hover">
                <Card.Img variant="top" src="PLACEHOLDER_IMAGE" />
                <Card.Body>
                  <Card.Title>Nombre Amigurumi</Card.Title>
                  <Card.Text className="text-muted">$15.000</Card.Text>
                  <Button variant="outline-dark" className="w-100 rounded-pill">Ver Detalle</Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>

    </div>
  );
};

export default Home;