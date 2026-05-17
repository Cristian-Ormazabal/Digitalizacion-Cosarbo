import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <Container className="text-center py-5 d-flex flex-column align-items-center justify-content-center" style={{ minHeight: '70vh' }}>
      <i className="bi bi-patch-question text-success" style={{ fontSize: '6rem', opacity: 0.5 }}></i>
      
      <h1 className="display-1 fw-bold text-success">404</h1>
      <h2 className="fw-bold mb-4">¡Ups! Parece que este hilo se enredó</h2>
      
      <p className="lead text-muted mb-5" style={{ maxWidth: '500px' }}>
        La página que buscas no existe o ha sido movida. No te preocupes, puedes volver al taller principal haciendo clic abajo.
      </p>

      <Button as={Link} to="/" variant="success" size="lg" className="rounded-pill px-5 shadow-sm btn-artesanal">
        Volver al Inicio
      </Button>
    </Container>
  );
};

export default NotFound;