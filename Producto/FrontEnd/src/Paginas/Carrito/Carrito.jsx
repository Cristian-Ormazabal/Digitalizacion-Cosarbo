import React, { useContext } from 'react';
import { Container, Table, Button, Row, Col, Card } from 'react-bootstrap';
import { CartContext } from '../../Context/CartContext'; // Cerebro carrito
import { Link } from 'react-router-dom';
import webpayLogo from '../../assets/1.Webpay_FB_80px.png';

const Carrito = () => {
  // Extracción de los datos y funciones que se necesitan del contexto
  const { cart, removeFromCart, clearCart } = useContext(CartContext);

  // Cálculo del total general
  const totalPagar = cart.reduce((acc, item) => acc + (item.precio * item.quantity), 0);

  return (
    <Container className="py-5" style={{ minHeight: '70vh' }}>
      <h2 className="mb-4 fw-bold text-success border-bottom pb-2">Tu Carrito de Compras</h2>

      {cart.length === 0 ? (
        // Mensaje si el carrito está vacío
        <div className="text-center py-5">
          <i className="bi bi-cart-x display-1 text-muted"></i>
          <h4 className="mt-3">Tu carrito está vacío</h4>
          <p className="text-muted">¿Aún no te decides? Tenemos amigurumis y servicios esperándote.</p>
          <Button as={Link} to="/catalogo" variant="success" className="rounded-pill px-4">
            Ir al Catálogo
          </Button>
        </div>
      ) : (
        <Row>
          {/* Listado de Productos */}
          <Col lg={8}>
            <Table responsive className="cart-table align-middle shadow-sm">
              <thead className="bg-light">
                <tr>
                  <th>Producto / Servicio</th>
                  <th>Precio</th>
                  <th className="text-center">Cant.</th>
                  <th>Subtotal</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {cart.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <div className="d-flex align-items-center">
                        <img src={item.imagen} alt={item.nombre} className="cart-img me-3 shadow-sm" />
                        <div>
                          <div className="fw-bold">{item.nombre}</div>
                          <small className="text-muted">{item.categoria}</small>
                        </div>
                      </div>
                    </td>
                    <td>${item.precio.toLocaleString('es-CL')}</td>
                    <td className="text-center">{item.quantity}</td>
                    <td className="fw-bold">${(item.precio * item.quantity).toLocaleString('es-CL')}</td>
                    <td className="text-end">
                      <Button 
                        variant="link" 
                        className="text-danger"
                        onClick={() => removeFromCart(item.id)}
                      >
                        <i className="bi bi-trash3-fill fs-5"></i>
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            
            <div className="d-flex justify-content-between mt-3">
              <Button variant="outline-secondary" className="rounded-pill" onClick={clearCart}>
                Vaciar Carrito
              </Button>
              <Button as={Link} to="/catalogo" variant="link" className="text-success text-decoration-none fw-bold">
                <i className="bi bi-arrow-left me-2"></i> Seguir Comprando
              </Button>
            </div>
          </Col>

          {/* Resumen de Compra */}
          <Col lg={4}>
            <Card className="cart-summary shadow border-0 p-3">
              <Card.Body>
                <h4 className="fw-bold mb-4">Resumen</h4>
                <div className="d-flex justify-content-between mb-3 fs-5">
                  <span>Productos ({cart.length})</span>
                  <span>${totalPagar.toLocaleString('es-CL')}</span>
                </div>
                <div className="d-flex justify-content-between mb-4 border-top pt-3 fs-4 fw-bold text-success">
                  <span>Total</span>
                  <span>${totalPagar.toLocaleString('es-CL')}</span>
                </div>
                
                <div className="bg-light p-3 rounded-3 mb-4 small">
                  <i className="bi bi-info-circle me-2"></i>
                  Los servicios de costura requieren validación de fecha presencial en el local de Maipú.
                </div>

                <Button 
                  className="btn-artesanal w-100 py-3 fs-5 rounded-pill shadow"
                  onClick={() => alert("")}
                >
                  Confirmar Pedido
                </Button>
                
                <div className="text-center mt-3">
                  <img src={webpayLogo} alt="Webpay" className="webpay-logo" />
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default Carrito;