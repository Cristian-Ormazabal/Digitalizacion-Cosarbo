import React, { useState, useEffect } from 'react';
import { Container, Tabs, Tab, Table, Badge, Card, Row, Col, Spinner } from 'react-bootstrap';
import api from '../../api/apiConfig'; 

export default function MisCompras() {
    const [pedidos, setPedidos] = useState([]);
    const [reservas, setReservas] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const cargarHistorial = async () => {
            try {
                const [resPedidos, resReservas] = await Promise.all([
                    api.get('/api/v1/pedidos/mis-pedidos'),
                    api.get('/api/v1/reservas/mis-reservas')
                ]);
                
                setPedidos(Array.isArray(resPedidos.data) ? resPedidos.data : []);
                setReservas(Array.isArray(resReservas.data) ? resReservas.data : []);
            } catch (error) {
                console.error("Error al cargar el historial de Cosarbo:", error);
            } finally {
                setLoading(false);
            }
        };

        cargarHistorial();
    }, []);

    const formatearFechaHora = (fechaString) => {
        if (!fechaString) return "";
        const fecha = new Date(fechaString);
        return fecha.toLocaleString('es-CL', { dateStyle: 'short', timeStyle: 'short' });
    };

    const formatearFechaCita = (fechaString) => {
        if (!fechaString) return "";
        const [anio, mes, dia] = fechaString.split('-');
        return `${dia}/${mes}/${anio}`;
    };

    if (loading) {
        return (
            <Container className="mt-5 text-center p-5">
                <Spinner animation="border" variant="success" />
                <h4 className="mt-3 text-muted">Cargando tu rincón Cosarbo... 🌸</h4>
            </Container>
        );
    }

    return (
        <Container className="mt-5 mb-5">
            <h2 className="mb-2 text-center text-success fw-bold">Mi Espacio Cosarbo 🌸</h2>
            <p className="text-muted text-center mb-5">Revisa el estado de tus amigurumis adquiridos y tus agendamientos de costura.</p>

            <Tabs defaultActiveKey="pedidos" id="perfil-usuario-tabs" className="mb-4 shadow-sm nav-fill">
                
                {/* PESTAÑA A: MIS AMIGURUMIS */}
                <Tab eventKey="pedidos" title="🛍️ Mis Pedidos (Amigurumis)">
                    {pedidos.length === 0 ? (
                        <div className="text-center p-5 bg-light rounded shadow-sm">
                            <h5 className="text-muted">Aún no tienes pedidos registrados. ¡Te invitamos a ver el catálogo! 🛒</h5>
                        </div>
                    ) : (
                        <Row>
                            {pedidos.map((pedido) => (
                                <Col md={12} key={pedido.idPedido} className="mb-4">
                                    <Card className="shadow-sm border-0 border-start border-success border-4">
                                        <Card.Header className="bg-dark text-white d-flex justify-content-between align-items-center py-3">
                                            <div>
                                                <strong className="fs-5">Pedido #PED-{pedido.idPedido}</strong>
                                                <span className="ms-3 text-light-50 fs-6">📅 {formatearFechaHora(pedido.fechaVenta)}</span>
                                            </div>
                                            <Badge bg="success" className="fs-6 p-2">
                                                Total Pagado: ${pedido.totalPagado?.toLocaleString('es-CL')}
                                            </Badge>
                                        </Card.Header>
                                        <Card.Body className="bg-white">
                                            <Table responsive hover className="align-middle mb-0">
                                                <thead className="table-light">
                                                    <tr className="text-secondary small text-uppercase">
                                                        <th>Producto</th>
                                                        <th>Cantidad</th>
                                                        <th>Precio Unitario</th>
                                                        <th className="text-end">Subtotal</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {pedido.carrito?.items?.map((item) => (
                                                        <tr key={item.idItem}>
                                                            <td>
                                                                <img 
                                                                    src={item.producto?.imagen || "https://via.placeholder.com/40"} 
                                                                    alt={item.producto?.nombre} 
                                                                    style={{ width: '40px', height: '40px', objectFit: 'cover', borderRadius: '5px' }} 
                                                                    className="me-3 shadow-sm"
                                                                />
                                                                <span className="fw-bold text-dark">{item.producto?.nombre}</span>
                                                            </td>
                                                            <td>{item.cantidad} u.</td>
                                                            <td>${item.producto?.precio?.toLocaleString('es-CL')}</td>
                                                            <td className="fw-bold text-end">${item.subTotal?.toLocaleString('es-CL')}</td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                            <hr className="text-muted" />
                                            <div className="text-muted small d-flex justify-content-between flex-wrap">
                                                <span>📍 <strong>Despacho a:</strong> {pedido.direccion}, {pedido.comuna}</span>
                                                <span className="mt-1 mt-sm-0">👤 <strong>Destinatario:</strong> {pedido.nombreReceptor}</span>
                                            </div>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>
                    )}
                </Tab>

                {/* PESTAÑA B: MIS HORAS DE COSTURA */}
                <Tab eventKey="reservas" title="🪡 Mis Horas Reservadas">
                    {reservas.length === 0 ? (
                        <div className="text-center p-5 bg-light rounded shadow-sm">
                            <h5 className="text-muted">No registras horas de costura agendadas por el momento. 🪡</h5>
                        </div>
                    ) : (
                        <Card className="border-0 shadow-sm">
                            <Card.Body className="p-0">
                                <Table striped responsive hover className="align-middle mb-0">
                                    <thead className="table-dark">
                                        <tr>
                                            <th>ID Cita</th>
                                            <th>Servicio / Prenda</th>
                                            <th>Fecha Cita</th>
                                            <th>Horario de Atención</th>
                                            <th>Tiempo Estimado</th>
                                            <th>Precio Base</th>
                                            <th className="text-center">Estado Reserva</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {reservas.map((res) => (
                                            <tr key={res.idReserva}>
                                                <td className="text-muted font-monospace">#RES-{res.idReserva}</td>
                                                {/* CORREGIDO: Usamos 'res.servicio' mapeando con exactitud tu Reserva.java */}
                                                <td className="fw-bold text-success">{res.servicio?.tipoPrenda}</td>
                                                <td className="fw-bold">📅 {formatearFechaCita(res.fechaReserva)}</td>
                                                <td>⏰ {res.horaReserva} hrs</td>
                                                <td>{res.servicio?.tiempoEstimado}</td>
                                                <td>${res.servicio?.costo?.toLocaleString('es-CL')}</td>
                                                <td className="text-center">
                                                    <Badge bg={
                                                        res.estado === 'Confirmada' ? 'success' : 
                                                        res.estado === 'Pendiente' ? 'warning' : 
                                                        res.estado === 'Completada' ? 'info' : 'danger'
                                                    } className="p-2 w-75">
                                                        {res.estado}
                                                    </Badge>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                    )}
                </Tab>
            </Tabs>
        </Container>
    );
}