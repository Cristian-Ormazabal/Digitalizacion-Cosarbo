import React, { useState } from 'react';
import { Container, Card, Form, Button, Row, Col } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';

const Registro = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
    console.log("Datos enviados:", formData);
    // Aquí irá la conexión al Backend
    alert("¡Registro exitoso! Ahora puedes iniciar sesión.");
    navigate('/login');
  };

  return (
    <Container className="d-flex align-items-center justify-content-center py-5" style={{ minHeight: '80vh' }}>
      <Card className="login-card shadow border-0 w-100" style={{ maxWidth: '500px' }}>
        <Card.Body className="p-4">
          <div className="text-center mb-4">
            <h2 className="fw-bold text-success">Crear Cuenta</h2>
            <p className="text-muted small">Únete a la comunidad de Cosarbo</p>
          </div>

          <Form onSubmit={handleSubmit}>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label className="small fw-bold">Nombre</Form.Label>
                  <Form.Control 
                    required
                    type="text" 
                    placeholder="Ej: Brayan" 
                    className="form-control-custom"
                    onChange={(e) => setFormData({...formData, nombre: e.target.value})}
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label className="small fw-bold">Apellido</Form.Label>
                  <Form.Control 
                    required
                    type="text" 
                    placeholder="Ej: Soto" 
                    className="form-control-custom"
                    onChange={(e) => setFormData({...formData, apellido: e.target.value})}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label className="small fw-bold">Correo Electrónico</Form.Label>
              <Form.Control 
                required
                type="email" 
                placeholder="correo@ejemplo.com" 
                className="form-control-custom"
                onChange={(e) => setFormData({...formData, email: e.target.value})}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label className="small fw-bold">Contraseña</Form.Label>
              <Form.Control 
                required
                type="password" 
                placeholder="********" 
                className="form-control-custom"
                onChange={(e) => setFormData({...formData, password: e.target.value})}
              />
            </Form.Group>

            <Form.Group className="mb-4">
              <Form.Label className="small fw-bold">Confirmar Contraseña</Form.Label>
              <Form.Control 
                required
                type="password" 
                placeholder="********" 
                className="form-control-custom"
                onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
              />
            </Form.Group>

            <Button type="submit" className="btn-artesanal w-100 rounded-pill py-2 mb-3 shadow-sm">
              Registrarse
            </Button>

            <div className="text-center small mt-3">
              ¿Ya tienes una cuenta? <Link to="/login" className="text-success fw-bold text-decoration-none">Inicia sesión aquí</Link>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Registro;