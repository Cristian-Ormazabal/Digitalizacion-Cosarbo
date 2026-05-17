import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { Modal, Button, Card, Container, Row, Col, ListGroup } from 'react-bootstrap';
import 'react-calendar/dist/Calendar.css';

const Servicios = () => {
    const BLOQUES_HORARIOS = ["09:00", "11:00", "15:00", "17:00", "19:00"];

    const [servicios, setServicios] = useState([]);
    const [servicioSeleccionado, setServicioSeleccionado] = useState(null);
    const [showModal, setShowModal] = useState(false);

    const [fechaSeleccionada, setFechaSeleccionada] = useState(new Date());
    const [diasAgotados, setDiasAgotados] = useState([]);
    const [horasOcupadas, setHorasOcupadas] = useState([]);
    const [horaSeleccionada, setHoraSeleccionada] = useState("");
    const [mensajeReserva, setMensajeReserva] = useState({ tipo: '', texto: '' });

    useEffect(() => {
        obtenerServicios();
        obtenerDiasAgotadosBackend();
    }, []);

    const obtenerServicios = async () => {
        try {
            const res = await api.get('/api/v1/servicios-costura');
            setServicios(res.data);
        } catch (err) {
            console.error("Error al cargar servicios:", err);
        }
    };

    const obtenerDiasAgotadosBackend = async () => {
        try {
            const res = await api.get('/api/v1/reservas/ocupadas');
            const fechasConfiguradas = res.data.map(fechaStr => new Date(fechaStr + 'T00:00:00'));
            setDiasAgotados(fechasConfiguradas);
        } catch (err) {
            console.error("Error al cargar días agotados:", err);
        }
    };

    const abrirModalReserva = (servicio) => {
        setServicioSeleccionado(servicio);
        setFechaSeleccionada(new Date());
        setHoraSeleccionada("");
        setHorasOcupadas([]);
        setMensajeReserva({ tipo: '', texto: '' });
        setShowModal(true);
        
        consultarHorasPorFecha(new Date());
    };

    const handleFechaChange = async (nuevaFecha) => {
        setFechaSeleccionada(nuevaFecha);
        setHoraSeleccionada(""); 
        setMensajeReserva({ tipo: '', texto: '' });
        await consultarHorasPorFecha(nuevaFecha);
    };

    const consultarHorasPorFecha = async (fecha) => {
        const offset = fecha.getTimezoneOffset();
        const fechaLocal = new Date(fecha.getTime() - (offset * 60 * 1000));
        const fechaFormateada = fechaLocal.toISOString().split('T')[0];

        try {
            const res = await api.get(`/api/v1/reservas/horas-ocupadas?fecha=${fechaFormateada}`);
            setHorasOcupadas(res.data); 
        } catch (err) {
            console.error("Error al traer horas ocupadas:", err);
        }
    };

    const tileDisabled = ({ date, view }) => {
        if (view === 'month') {
            return diasAgotados.some(diasOcupados => 
                date.getDate() === diasOcupados.getDate() &&
                date.getMonth() === diasOcupados.getMonth() &&
                date.getFullYear() === diasOcupados.getFullYear()
            );
        }
        return false;
    };

    const ejecutarReserva = async () => {
        if (!horaSeleccionada) {
            setMensajeReserva({ tipo: 'danger', texto: 'Por favor, selecciona una hora para la atención.' });
            return;
        }

        const idUsuario = localStorage.getItem('userId'); 
        const token = localStorage.getItem('token');

        if (!idUsuario) {
            setMensajeReserva({ tipo: 'danger', texto: 'Debes iniciar sesión para agendar un servicio.' });
            return;
        }

        if (!token) {
            setMensajeReserva({ tipo: 'danger', texto: 'Error de autenticación: No se encontró un token válido. Por favor, re-inicia sesión.' });
            return;
        }

        const offset = fechaSeleccionada.getTimezoneOffset();
        const fechaLocal = new Date(fechaSeleccionada.getTime() - (offset * 60 * 1000));
        const fechaFormateada = fechaLocal.toISOString().split('T')[0];

        const payload = {
            idUsuario: parseInt(idUsuario), 
            idServicio: servicioSeleccionado.idServicio,
            fechaReserva: fechaFormateada,
            horaReserva: horaSeleccionada
        };

        try {
            await api.post('/api/v1/reservas/agendar', payload, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            setMensajeReserva({ tipo: 'success', texto: '¡Reserva agendada con éxito! 🧵✨' });
            
            obtenerDiasAgotadosBackend();
            
            setTimeout(() => {
                setShowModal(false);
            }, 2000);

        } catch (err) {
            const msgError = err.response?.data || "Error interno al procesar la cita.";
            setMensajeReserva({ tipo: 'danger', texto: msgError });
        }
    };

    return (
        <Container className="py-5">
            <div className="text-center mb-5">
                <h2 className="display-5 fw-bold text-success">Servicios de Costura</h2>
                <p className="lead text-muted">Agenda tu atención presencial en nuestro taller de Maipú</p>
                <div className="mx-auto bg-success" style={{ height: '4px', width: '60px' }}></div>
            </div>

            <Row>
                {servicios.map(servicio => (
                    <Col key={servicio.idServicio} md={4} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm card-hover">
                            <Card.Body className="d-flex flex-column">
                                <div className="d-flex justify-content-between mb-2">
                                    <Card.Title className="fw-bold">{servicio.tipoPrenda}</Card.Title>
                                </div>
                                <Card.Text className="text-muted flex-grow-1">
                                    {servicio.descripcion}
                                </Card.Text>
                                <ListGroup variant="flush" className="big mb-3">
                                    <ListGroup.Item className="px-0"><i className="bi bi-clock me-2"></i>Tiempo: {servicio.tiempoEstimado}</ListGroup.Item>
                                    <ListGroup.Item className="px-0 text-success fw-bold"><i className="bi bi-tag me-2"></i>Desde: ${servicio.costo?.toLocaleString('es-CL')}</ListGroup.Item>
                                </ListGroup>
                                <div className="d-flex justify-content-between align-items-center mt-3">

                                    <Button variant="outline-success" 
                                            className="rounded-pill fw-bold" 
                                            onClick={() => abrirModalReserva(servicio)}>
                                        Reservar Cita
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>

            {/* Modal de Agendamiento */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered size="md">
                <Modal.Header closeButton>
                    <Modal.Title className="fw-bold fs-5">
                        Agendar: {servicioSeleccionado?.nombre}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body className="d-flex flex-column align-items-center">
                    
                    {mensajeReserva.texto && (
                        <div className={`alert alert-${mensajeReserva.tipo} w-100 text-center`} role="alert">
                            {mensajeReserva.texto}
                        </div>
                    )}

                    <label className="fw-bold text-secondary mb-2 align-self-start px-3">
                        1. Selecciona el día disponible:
                    </label>
                    
                    <Calendar 
                        onChange={handleFechaChange}
                        value={fechaSeleccionada}
                        tileDisabled={tileDisabled}
                        minDate={new Date()}
                        className="shadow-sm rounded border-0 custom-calendar"
                    />

                    <div className="mt-4 w-100 px-3">
                        <label className="form-label fw-bold text-secondary">
                            2. Horarios disponibles para este día:
                        </label>
                        <select 
                            className="form-select select-horario" 
                            value={horaSeleccionada} 
                            onChange={(e) => setHoraSeleccionada(e.target.value)}
                        >
                            <option value="">-- Elige una hora --</option>
                            {BLOQUES_HORARIOS.map(hora => {
                                const estaOcupado = horasOcupadas.includes(hora);
                                return (
                                    <option key={hora} value={hora} disabled={estaOcupado}>
                                        {hora} {estaOcupado ? " 🧵 (Ocupado)" : " ✨ (Disponible)"}
                                    </option>
                                );
                            })}
                        </select>
                    </div>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Cancelar
                    </Button>
                    <Button variant="success" onClick={ejecutarReserva}>
                        Confirmar Cita
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default Servicios;