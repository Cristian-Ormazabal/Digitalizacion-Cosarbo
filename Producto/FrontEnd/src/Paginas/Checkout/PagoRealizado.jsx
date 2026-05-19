import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { useParams, Link } from 'react-router-dom';
import { Container, Card, Table, Button, Spinner, Alert } from 'react-bootstrap';

const formatearPrecio = (precio) => {
    return new Intl.NumberFormat('es-CL', {
        style: 'currency',
        currency: 'CLP'
    }).format(precio);
};

export default function PagoRealizado() {
    const { ordenId } = useParams();
    
    const [items, setItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchDetalleCompra = async () => {
            try {
                const response = await api.get(`/api/v1/items-carrito/carrito/${ordenId}`);
                
                if (response.data) {
                    setItems(response.data);
                }
                setIsLoading(false);
            } catch (err) {
                console.error("Error al cargar el resumen:", err);
                setError("No pudimos recuperar el detalle de tu compra, pero tu pedido ya está en proceso.");
                setIsLoading(false);
            }
        };

        if (ordenId) {
            fetchDetalleCompra();
        }
    }, [ordenId]);

    const totalPagado = items.reduce((acc, item) => acc + (item.subTotal), 0);

    if (isLoading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" variant="success" />
                <p className="mt-3">Generando tu comprobante de Cosarbo...</p>
            </Container>
        );
    }

    return (
        <Container className="mt-5 mb-5" style={{ maxWidth: '800px' }}>
            <Card className="border-0 shadow-lg p-4">
                <div className="text-center mb-4">
                    <div className="display-1 text-success" style={{ lineHeight: '1' }}>✓</div>
                    <h2 className="fw-bold mt-2">¡Muchas gracias por tu compra!</h2>
                    <p className="text-muted">Pedido Confirmado #COS-{ordenId}</p>
                    
                    <Alert variant="success" className="border-0 shadow-sm mt-3">
                        Tu pedido ha sido recibido con éxito. Pronto recibirás un correo con los detalles para el despacho en Maipú.
                    </Alert>
                </div>

                <div className="resumen-seccion mt-4">
                    <h5 className="fw-bold mb-3 border-bottom pb-2 text-success">
                        <i className="bi bi-basket2-fill me-2"></i>Resumen de Productos
                    </h5>
                    
                    {error ? (
                        <Alert variant="warning">{error}</Alert>
                    ) : (
                        <Table responsive hover className="align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th>Amigurumi</th>
                                    <th className="text-center">Cant.</th>
                                    <th className="text-end">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {items.length > 0 ? (
                                    items.map((item) => (
                                        <tr key={item.idItem}>
                                            <td>
                                                <div className="fw-bold">{item.producto?.nombre || "Producto Artesanal"}</div>
                                                <small className="text-muted">Tejido a mano con amor</small>
                                            </td>
                                            <td className="text-center">{item.cantidad}</td>
                                            <td className="text-end">{formatearPrecio(item.subTotal)}</td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="3" className="text-center text-muted py-4">
                                            No se encontraron detalles para este pedido.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                            <tfoot className="table-group-divider">
                                <tr className="h5">
                                    <td colSpan="2" className="text-end fw-bold">Total Pagado:</td>
                                    <td className="text-end fw-bold text-success">{formatearPrecio(totalPagado)}</td>
                                </tr>
                            </tfoot>
                        </Table>
                    )}
                </div>

                <div className="d-flex justify-content-between align-items-center mt-5 pt-3 border-top">
                    <Button variant="outline-secondary" onClick={() => window.print()} className="rounded-pill px-4">
                        <i className="bi bi-printer me-2"></i>Imprimir Boleta
                    </Button>
                    <Button as={Link} to="/catalogo" variant="success" className="rounded-pill px-4 fw-bold shadow-sm">
                        Seguir Comprando
                    </Button>
                </div>
                
                <div className="text-center mt-4">
                    <small className="text-muted">
                        ¿Tienes dudas? Contáctanos a través de nuestras redes sociales de Cosarbo.
                    </small>
                </div>
            </Card>
        </Container>
    );
}