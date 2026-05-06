import React from 'react';
import { Container, Card, Form, Button } from 'react-bootstrap';

const Login = () => {
  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: '70vh' }}>
      <Card className="login-card shadow border-0">
        <h2 className="text-center mb-4">Ingresar</h2>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Correo electrónico</Form.Label>
            <Form.Control type="email" placeholder="nombre@ejemplo.com" className="form-control-custom" />
          </Form.Group>
          <Form.Group className="mb-4">
            <Form.Label>Contraseña</Form.Label>
            <Form.Control type="password" placeholder="********" className="form-control-custom" />
          </Form.Group>
          <Button className="btn-artesanal w-100 rounded-pill py-2 mb-3">Iniciar Sesión</Button>
          <div className="text-center fs-7 text-muted">
            ¿No tienes cuenta? <a href="/registro" className="text-decoration-none">Regístrate aquí</a>
          </div>
        </Form>
      </Card>
    </Container>
  );
};

export default Login;