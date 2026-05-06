import React from 'react';
import { Container, Row, Col, Card, Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Catalogo = () => {
  const navigate = useNavigate();

  return (
    <Container className="catalogo-container py-5">
      <Row>
        {/* Sidebar de Filtros */}
        <Col md={3}>
          <div className="sidebar-filtros shadow-sm mb-4">
            <h5 className="filtro-titulo">Categorías</h5>
            <Form>
              {['Amigurumis', 'Costura', 'Accesorios'].map((cat) => (
                <Form.Check key={cat} type="checkbox" label={cat} className="mb-2" />
              ))}
              <h5 className="filtro-titulo mt-4">Precio</h5>
              <Form.Range />
              <Button variant="outline-dark" className="w-100 mt-3 rounded-pill">Filtrar</Button>
            </Form>
          </div>
        </Col>

        {/* Grilla de Productos */}
        <Col md={9}>
          <h2 className="mb-4">Nuestra Colección</h2>
          <Row>
            {[1, 2, 3, 4, 5, 6].map((p) => (
              <Col key={p} md={4} className="mb-4">
                <Card className="card-product shadow-sm">
                  <Card.Img variant="top" src="https://placehold.co/400x400?text=Amigurumi" />
                  <Card.Body className="text-center">
                    <Card.Title>Producto {p}</Card.Title>
                    <Card.Text className="fw-bold text-muted">$10.000</Card.Text>
                    <Button className="btn-artesanal rounded-pill w-100" onClick={() => navigate(`/producto/${p}`)}>
                      Ver Detalles
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </Col>
      </Row>
    </Container>
  );
};

export default Catalogo;