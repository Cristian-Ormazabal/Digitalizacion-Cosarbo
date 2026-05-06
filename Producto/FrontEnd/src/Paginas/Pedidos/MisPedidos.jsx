import React from 'react';
import { Container, Row, Col, Card, Badge, Table, Button } from 'react-bootstrap';

const MisPedidos = () => {
  // Datos de ejemplo
  const pedidos = [
    {
      id: "COS-1024",
      fecha: "02/05/2026",
      tipo: "Servicio",
      detalle: "Basta de Pantalón Jean",
      total: 5000,
      estado: "En Proceso", // Los estados: Recibido, En Proceso, Listo, Entregado
      color: "warning"
    },
    {
      id: "COS-1021",
      fecha: "28/04/2026",
      tipo: "Producto",
      detalle: "Amigurumi Dragoncito Verde",
      total: 12990,
      estado: "Entregado",
      color: "secondary"
    },
    {
      id: "COS-1025",
      fecha: "04/05/2026",
      tipo: "Servicio",
      detalle: "Ajuste de Cintura",
      total: 8500,
      estado: "Recibido",
      color: "info"
    }
  ];

  return (
    <Container className="py-5" style={{ minHeight: '70vh' }}>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="fw-bold text-success">Mis Pedidos y Servicios</h2>
        <Badge bg="dark" className="p-2">Usuario: Brayan Soto</Badge>
      </div>

      <Row>
        <Col lg={12}>
          <Card className="border-0 shadow-sm rounded-4 overflow-hidden">
            <Table responsive hover className="mb-0 align-middle">
              <thead className="bg-success text-white">
                <tr>
                  <th className="py-3 ps-4">ID Pedido</th>
                  <th className="py-3">Fecha</th>
                  <th className="py-3">Tipo</th>
                  <th className="py-3">Detalle</th>
                  <th className="py-3">Total</th>
                  <th className="py-3 text-center">Estado</th>
                  <th className="py-3 text-center">Acción</th>
                </tr>
              </thead>
              <tbody>
                {pedidos.map((p) => (
                  <tr key={p.id}>
                    <td className="ps-4 fw-bold">{p.id}</td>
                    <td>{p.fecha}</td>
                    <td>
                      <Badge bg={p.tipo === 'Servicio' ? 'info' : 'primary'} pill>
                        {p.tipo}
                      </Badge>
                    </td>
                    <td>{p.detalle}</td>
                    <td className="fw-bold">${p.total.toLocaleString('es-CL')}</td>
                    <td className="text-center">
                      <Badge bg={p.color} className="p-2 w-100">
                        {p.estado}
                      </Badge>
                    </td>
                    <td className="text-center">
                      <Button variant="outline-success" size="sm" className="rounded-pill">
                        Ver Comprobante
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Card>
        </Col>
      </Row>

      {/* Informativo */}
      <div className="mt-5 p-4 bg-light rounded-4 border">
        <h5 className="fw-bold text-dark"><i className="bi bi-info-circle-fill me-2"></i>Información sobre tus Servicios de Costura</h5>
        <p className="small text-muted mb-0">
          Los servicios marcados como <strong>"Listo"</strong> pueden ser retirados en nuestro taller de Maipú presentando tu ID de pedido o el comprobante digital. Recuerda que los tiempos de entrega pueden variar según la complejidad del ajuste.
        </p>
      </div>
    </Container>
  );
};

export default MisPedidos;