import React from 'react';
import { Container, Row, Col, Card, Image } from 'react-bootstrap';

const Nosotros = () => {
  return (
    <Container className="py-5">
      {/* SECCIÓN 1: CABECERA E HISTORIA */}
      <Row className="align-items-center mb-5">
        <Col lg={6} className="mb-4 mb-lg-0">
          <h1 className="display-4 fw-bold text-success mb-4">Nuestra Historia</h1>
          <p className="lead text-secondary">
            Cosarbo nació en el corazón de <strong>Maipú</strong>, bajo la premisa de que cada prenda tiene una historia y cada regalo debe tener alma.
          </p>
          <p>
            Lo que comenzó como un pequeño taller familiar de costura, evolucionó al integrar el arte del crochet y los amigurumis. 
            Hoy, combinamos la precisión técnica de la sastrería con la ternura de los juguetes tejidos a mano, ofreciendo soluciones 
            locales para una comunidad que valora la durabilidad y el detalle.
          </p>
        </Col>
        <Col lg={6}>
          <Image 
            src="https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?q=80&w=1000" 
            alt="Taller de costura" 
            fluid 
            className="rounded-5 shadow-lg"
          />
        </Col>
      </Row>

      {/* SECCIÓN 2: VALORES (CARDS) */}
      <Row className="py-5 bg-light rounded-5 text-center g-4 mb-5 shadow-sm">
        <Col md={4}>
          <div className="p-3">
            <i className="bi bi-heart-fill fs-1 text-danger"></i>
            <h4 className="mt-3 fw-bold">Hecho con Amor</h4>
            <p className="text-muted small">Cada punto y cada costura se realiza con dedicación exclusiva.</p>
          </div>
        </Col>
        <Col md={4}>
          <div className="p-3">
            <i className="bi bi-geo-alt-fill fs-1 text-success"></i>
            <h4 className="mt-3 fw-bold">Identidad Local</h4>
            <p className="text-muted small">Orgullosos de trabajar desde Maipú para toda la Región Metropolitana.</p>
          </div>
        </Col>
        <Col md={4}>
          <div className="p-3">
            <i className="bi bi-recycle fs-1 text-primary"></i>
            <h4 className="mt-3 fw-bold">Segunda Vida</h4>
            <p className="text-muted small">Promovemos la moda sostenible reparando tus prendas favoritas.</p>
          </div>
        </Col>
      </Row>

      {/* SECCIÓN 3: EL EQUIPO */}
      <div className="text-center mb-5">
        <h2 className="fw-bold">El Equipo detrás de Cosarbo</h2>
        <p className="text-muted">Uniendo tecnología y tradición artesanal.</p>
      </div>
      <Row className="justify-content-center g-4">
        <Col md={4}>
          <Card className="border-0 shadow-sm text-center p-3 h-100 card-hover">
            <Card.Body>
              <div className="bg-success text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3" style={{width: '80px', height: '80px'}}>
                <i className="bi bi-person-fill fs-2"></i>
              </div>
              <Card.Title className="fw-bold">Gestión y Análisis</Card.Title>
              <Card.Text className="small text-muted">Asegurando que la experiencia del cliente sea fluida y profesional.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="border-0 shadow-sm text-center p-3 h-100 card-hover">
            <Card.Body>
              <div className="bg-warning text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3" style={{width: '80px', height: '80px'}}>
                <i className="bi bi-palette-fill fs-2"></i>
              </div>
              <Card.Title className="fw-bold">Diseño y Frontend</Card.Title>
              <Card.Text className="small text-muted">Creando interfaces modernas para técnicas tradicionales.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Nosotros;