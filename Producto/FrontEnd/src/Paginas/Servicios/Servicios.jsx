import React from 'react';
import { Container, Row, Col, Card, Button, Badge, ListGroup } from 'react-bootstrap';

const Servicios = () => {
  const serviciosCostura = [
    { id: 1, nombre: "Bastas y Ajustes", precio: "Desde $5.000", tiempo: "24-48 hrs", cupos: "Disponibles" },
    { id: 2, nombre: "Rebaje de Cintura", precio: "Desde $8.000", tiempo: "48 hrs", cupos: "Pocos cupos" },
    { id: 3, nombre: "Cambio de Cierre", precio: "Desde $4.500", tiempo: "24 hrs", cupos: "Agotado hoy" }
  ];

  return (
    <Container className="py-5">
      <h2 className="mb-4 text-success border-bottom pb-2">Servicios de Costura Profesional</h2>
      <Row>
        <Col lg={8}>
          <Row>
            {serviciosCostura.map((s) => (
              <Col md={6} key={s.id} className="mb-4">
                <Card className="shadow-sm h-100 border-0">
                  <Card.Body>
                    <div className="d-flex justify-content-between align-items-start">
                      <Card.Title className="fw-bold">{s.nombre}</Card.Title>
                      <Badge bg={s.cupos === "Agotado hoy" ? "danger" : "success"}>{s.cupos}</Badge>
                    </div>
                    <Card.Text className="text-muted mt-2">
                      Realizamos ajustes precisos para que tu prenda quede a medida. Incluye toma de medidas presencial.
                    </Card.Text>
                    <ListGroup variant="flush" className="small mb-3">
                      <ListGroup.Item><i className="bi bi-clock me-2"></i>Tiempo estimado: {s.tiempo}</ListGroup.Item>
                      <ListGroup.Item><i className="bi bi-tag me-2"></i>Valor aproximado: {s.precio}</ListGroup.Item>
                    </ListGroup>
                    <Button variant="outline-success" className="w-100 rounded-pill">Ver fechas disponibles</Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </Col>

        {/* Sidebar Informativo */}
        <Col lg={4}>
          <div className="p-4 bg-light rounded-4 shadow-sm border">
            <h5><i className="bi bi-info-circle me-2"></i>¿Cómo funciona?</h5>
            <ol className="small mt-3">
              <li>Revisa el servicio y el valor base.</li>
              <li>Consulta la disponibilidad de fechas.</li>
              <li>Acude a nuestra tienda en Maipú para la recepción de la prenda.</li>
              <li>Te notificaremos cuando esté lista para retiro.</li>
            </ol>
            <Button variant="success" className="w-100 mt-3 rounded-pill">Consultar por WhatsApp</Button>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Servicios;