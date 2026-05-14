import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css'; // Estilos necesarios para el calendario
import { Container, Row, Col, Card, Button, Badge, ListGroup, Spinner, Alert, Modal } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Servicios = () => {
    const [servicios, setServicios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    // Estados para el Modal y el Calendario
    const [showModal, setShowModal] = useState(false);
    const [servicioSeleccionado, setServicioSeleccionado] = useState(null);
    const [fechaSeleccionada, setFechaSeleccionada] = useState(new Date());
    const [fechasOcupadas, setFechasOcupadas] = useState([]);
    const [agendando, setAgendando] = useState(false);

    useEffect(() => {
        const fetchServicios = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/servicios-costura');
                setServicios(response.data);
                setLoading(false);
            } catch (err) {
                console.error("Error al cargar servicios:", err);
                setError("No pudimos conectar con el taller de Cosarbo.");
                setLoading(false);
            }
        };
        fetchServicios();
    }, []);

    // Función para abrir el calendario y cargar fechas ocupadas
    const abrirAgenda = async (servicio) => {
        setServicioSeleccionado(servicio);
        try {
            const res = await axios.get('http://localhost:8080/api/v1/reservas/ocupadas');
            // Convertimos las fechas del backend a strings para comparar fácilmente
            const ocupadas = res.data.map(fecha => 
                {
                    const [year, month, day] = fecha.split('-').map(Number);
                    return new Date(year, month - 1, day).toDateString();
                });
            setFechasOcupadas(ocupadas);
            setShowModal(true);
        } catch (err) {
            alert("Error al consultar disponibilidad.");
        }
    };

    // Lógica para deshabilitar días ocupados y fines de semana (opcional)
    const tileDisabled = ({ date, view }) => {
        if (view === 'month') {
            // Deshabilitar si la fecha está en la lista de ocupadas o es domingo
            return fechasOcupadas.includes(date.toDateString()) || date.getDay() === 0;
        }
    };

    const handleAgendar = async () => {
        const userId = localStorage.getItem('user_id');
        if (!userId) {
            alert("Debes iniciar sesión para agendar una hora.");
            navigate('/login');
            return;
        }

        setAgendando(true);

        const offset = fechaSeleccionada.getTimezoneOffset();
        const fechaLocal = new Date(fechaSeleccionada.getTime() - (offset * 60 * 1000));
        const fechaFormateada = fechaLocal.toISOString().split('T')[0];

        try {
            const reserva = {
                idUsuario: parseInt(userId),
                idServicio: servicioSeleccionado.idServicio,
                fechaReserva: fechaFormateada // Formato YYYY-MM-DD
            };

            await axios.post('http://localhost:8080/api/v1/reservas/agendar', reserva);
            alert(`¡Cita confirmada! Te esperamos el ${fechaSeleccionada.toLocaleDateString()} para tu ${servicioSeleccionado.tipoPrenda}.`);
            setShowModal(false);
        } catch (err) {
            console.error(err);
            alert("Hubo un error al procesar tu reserva.");
        } finally {
            setAgendando(false);
        }
    };

    if (loading) return <Container className="text-center py-5"><Spinner animation="border" variant="success" /></Container>;

    return (
        <Container className="py-5">
            <div className="text-center mb-5">
                <h2 className="display-5 fw-bold text-success">Servicios de Costura</h2>
                <p className="lead text-muted">Agenda tu atención presencial en nuestro taller de Maipú</p>
                <div className="mx-auto bg-success" style={{ height: '4px', width: '60px' }}></div>
            </div>

            {error && <Alert variant="danger" className="text-center">{error}</Alert>}

            <Row>
                <Col lg={8}>
                    <Row>
                        {servicios.map((s) => (
                            <Col md={6} key={s.idServicio} className="mb-4">
                                <Card className="h-100 border-0 shadow-sm card-hover">
                                    <Card.Body className="d-flex flex-column">
                                        <div className="d-flex justify-content-between mb-2">
                                            <Card.Title className="fw-bold">{s.tipoPrenda}</Card.Title>
                                            <Badge bg={s.estadoCupo === 'Disponible' ? 'success' : 'warning'}>{s.estadoCupo}</Badge>
                                        </div>
                                        <Card.Text className="text-muted small flex-grow-1">{s.descripcion}</Card.Text>
                                        <ListGroup variant="flush" className="small mb-3">
                                            <ListGroup.Item className="px-0"><i className="bi bi-clock me-2"></i>Tiempo: {s.tiempoEstimado}</ListGroup.Item>
                                            <ListGroup.Item className="px-0 text-success fw-bold"><i className="bi bi-tag me-2"></i>Desde: ${s.costo?.toLocaleString('es-CL')}</ListGroup.Item>
                                        </ListGroup>
                                        <Button 
                                            variant="outline-success" 
                                            className="rounded-pill fw-bold"
                                            onClick={() => abrirAgenda(s)}
                                            disabled={s.estadoCupo === 'Agotado hoy'}
                                        >
                                            Ver fechas disponibles
                                        </Button>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Col>

                <Col lg={4}>
                    <div className="p-4 bg-light rounded-4 border sticky-top" style={{ top: '20px' }}>
                        <h5 className="fw-bold text-success"><i className="bi bi-calendar-check me-2"></i>¿Cómo agendar?</h5>
                        <ul className="small mt-3 list-unstyled">
                            <li className="mb-2">✅ Selecciona el servicio.</li>
                            <li className="mb-2">📅 Elige un día disponible en el calendario.</li>
                            <li className="mb-2">📍 Acude a nuestro taller en Maipú el día acordado.</li>
                            <li>🧵 Realizamos tu arreglo con la mejor calidad.</li>
                        </ul>
                    </div>
                </Col>
            </Row>

            {/* MODAL DEL CALENDARIO */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Agendar {servicioSeleccionado?.tipoPrenda}</Modal.Title>
                </Modal.Header>
                <Modal.Body className="d-flex flex-column align-items-center">
                    <Calendar 
                        onChange={setFechaSeleccionada} 
                        value={fechaSeleccionada}
                        tileDisabled={tileDisabled}
                        minDate={new Date()}
                        className="border-0 shadow-sm rounded"
                    />
                    <div className="mt-4 text-center">
                        <p className="mb-1">Día de atención seleccionado:</p>
                        <h5 className="text-success fw-bold">{fechaSeleccionada.toLocaleDateString('es-CL', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}</h5>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Cancelar</Button>
                    <Button 
                        variant="success" 
                        onClick={handleAgendar}
                        disabled={agendando}
                    >
                        {agendando ? 'Procesando...' : 'Confirmar Cita'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default Servicios;