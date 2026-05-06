import React, { useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Button, Form, Badge, Card } from 'react-bootstrap';
import { CartContext } from '../../Context/CartContext.jsx';

const DetallesProductos = () => {
  const { id } = useParams(); // Aquí captura el ID de la URL
  const navigate = useNavigate();
  const { addToCart } = useContext(CartContext);
  const [cantidad, setCantidad] = useState(1);

  // Datos de ejemplo
  const producto = {
    id: id,
    nombre: "Dragoncito Crochet Verde",
    precio: 12990,
    descripcion: "Amigurumi tejido a mano con hilo de algodón hipoalergénico. Ideal para regalo o decoración de habitaciones infantiles.",
    stock: 5,
    categoria: "Amigurumi",
    imagen: "https://placehold.co/600x600?text=Dragoncito+Verde"
  };

  const handleAgregarCarrito = () => {
    console.log(`Agregado al carrito: ${producto.nombre} - Cantidad: ${cantidad}`);
    addToCart(producto, cantidad);
    alert(`Has añadido ${cantidad} ${producto.nombre}(s) al carrito.`);
  };

  return (
    <Container className="py-5">
      <Row className="gy-4">
        {/* Columna de Imagen */}
        <Col md={6}>
          <Card className="border-0 shadow-sm overflow-hidden rounded-4">
            <Card.Img src={producto.imagen} alt={producto.nombre} />
          </Card>
        </Col>

        {/* Columna de Información */}
        <Col md={6}>
          <div className="ps-md-4">
            <Badge bg="success" className="mb-2 rounded-pill px-3 py-2">
              {producto.categoria}
            </Badge>
            <h2 className="display-5 fw-bold mb-3">{producto.nombre}</h2>
            <h3 className="text-muted mb-4">${producto.precio.toLocaleString('es-CL')}</h3>
            
            <hr />
            
            <p className="lead fs-6 text-secondary mb-4">
              {producto.descripcion}
            </p>

            {producto.categoria === "Amigurumi" ? (
              // Vista para Productos Físicos
              <div className="mb-4">
                <Form.Group className="mb-3" style={{ maxWidth: '150px' }}>
                  <Form.Label className="fw-bold">Cantidad</Form.Label>
                  <Form.Control 
                    type="number" 
                    value={cantidad} 
                    min="1" 
                    max={producto.stock}
                    onChange={(e) => setCantidad(e.target.value)}
                    className="rounded-pill border-2"
                  />
                  <Form.Text className="text-muted">
                    {producto.stock} unidades disponibles
                  </Form.Text>
                </Form.Group>
                
                <Button 
                  variant="success" 
                  size="lg" 
                  className="w-100 rounded-pill py-3 fw-bold shadow-sm"
                  onClick={handleAgregarCarrito}
                >
                  <i className="bi bi-cart-plus me-2"></i> Añadir al Carrito
                </Button>
              </div>
            ) : (
              // Vista para Servicios de Costura
              <div className="bg-light p-4 rounded-4 border border-2 border-dashed border-success">
                <h5 className="fw-bold"><i className="bi bi-calendar-check me-2"></i>Información del Servicio</h5>
                <p className="small mb-3">Este servicio requiere entrega presencial en nuestro taller en Maipú.</p>
                <Button 
                  as="a" 
                  href="https://wa.me/tu-numero" 
                  variant="outline-success" 
                  className="w-100 rounded-pill fw-bold"
                >
                  Consultar Disponibilidad por WhatsApp
                </Button>
              </div>
            )}

            <div className="mt-5">
              <h6><i className="bi bi-shield-check me-2 text-success"></i> Compra Segura</h6>
              <p className="small text-muted">Retiro en local disponible en 24 horas hábiles una vez confirmada la compra.</p>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default DetallesProductos;