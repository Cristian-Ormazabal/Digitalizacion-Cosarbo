import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

const Footer = () => {
  return (
    <footer className="footer-cosarbo mt-auto">
      <Container>
        <Row className="gy-4">
          <Col md={4}>
            <h3>Cosarbo</h3>
            <p className="mt-3">Tejiendo sueños y cosiendo realidades. Productos 100% artesanales hechos con amor en Chile.</p>
          </Col>
          <Col md={4} className="text-md-center">
            <h5>Enlaces Rápidos</h5>
            <ul className="list-unstyled">
              <li><a href="/" className="footer-link">Inicio</a></li>
              <li><a href="/catalogo" className="footer-link">Catálogo</a></li>
              <li><a href="/carrito" className="footer-link">Mi Carrito</a></li>
              <li><a href="/servicios" className="footer-link">Servicios</a></li>
              <li><a href="/nosotros" className="footer-link">Nosotros</a></li>
            </ul>
          </Col>
          <Col md={4} className="text-md-end">
            <h5>Contacto</h5>
            <p className="mb-1">Maipú, Santiago, Chile</p>
            <div className="fs-3 mt-2">
              <i className="bi bi-instagram me-3 cursor-pointer"></i>
              <i className="bi bi-whatsapp me-3 cursor-pointer"></i>
              <i className="bi bi-facebook cursor-pointer"></i>
            </div>
          </Col>
        </Row>
        <hr className="mt-4 bg-light" />
        <p className="text-center mb-0 pb-3">&copy; 2026 Cosarbo - Portafolio de Título.</p>
      </Container>
    </footer>
  );
};

export default Footer;