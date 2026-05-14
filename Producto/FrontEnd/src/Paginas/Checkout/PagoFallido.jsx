import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Container, Card, Button, Alert } from 'react-bootstrap';

export default function PagoFallido() {
    const location = useLocation();
    const navigate = useNavigate();
    const { formData } = location.state || {};

    return (
        <Container className="mt-5 text-center" style={{ maxWidth: '600px' }}>
            <Card className="border-0 shadow p-5">
                <div className="display-1 text-danger mb-3">✕</div>
                <h2 className="fw-bold text-danger">Pago No Procesado</h2>
                <p className="text-muted">
                    Lo sentimos, {formData?.nombre || 'estimado cliente'}, no pudimos procesar tu transacción en este momento.
                </p>
                <Alert variant="warning">
                    No se ha realizado ningún cargo a tu cuenta. Puede ser un problema de conexión con el banco.
                </Alert>
                <div className="d-grid gap-2 mt-4">
                    <Button variant="danger" size="lg" onClick={() => navigate('/checkout')}>
                        Reintentar Pago
                    </Button>
                    <Button variant="outline-secondary" onClick={() => navigate('/catalogo')}>
                        Volver al Catálogo
                    </Button>
                </div>
            </Card>
        </Container>
    );
}