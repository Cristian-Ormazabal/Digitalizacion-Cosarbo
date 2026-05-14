import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import api from '../../api/apiConfig';
import { Container, Table, Button, Badge, Card, Spinner, Row, Col, Modal, Form, Tabs, Tab } from 'react-bootstrap';

export default function AdminPanel() {
    const [pedidos, setPedidos] = useState([]);
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [servicios, setServicios] = useState([]);
    const [showModalServicio, setShowModalServicio] = useState(false);
    const [servicioEdit, setServicioEdit] = useState({
        idServicio: null,
        tipoPrenda: '',
        costo: '',
        tiempoEstimado: '',
        estadoCupo: 'Disponible'
    });
    
    // Estado para edición/creación de producto
    const [productoEdit, setProductoEdit] = useState({
        idProducto: null,
        nombre: '',
        descripcion: '',
        precio: '',
        stock: '',
        imagen: ''
    });

    useEffect(() => {
        cargarDatos();
    }, []);

    const cargarDatos = async () => {
        setLoading(true);
        try {
            const [resPedidos, resProductos, resServicios] = await Promise.all([
                api.get('/api/v1/pedidos'),
                api.get('/api/v1/productos'),
                api.get('/api/v1/servicios-costura') // Tu nuevo endpoint
            ]);
            setPedidos(resPedidos.data);
            setProductos(resProductos.data);
            setServicios(resServicios.data);
            setLoading(false);
        } catch (error) {
            console.error("Error al cargar datos:", error);
            setLoading(false);
        }
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            // Si tiene idProducto, es edición (PUT), si no, es nuevo (POST)
            if (productoEdit.idProducto) {
                await api.put(`/api/v1/productos/${productoEdit.idProducto}`, productoEdit);
            } else {
                await api.post('/api/v1/productos', productoEdit);
            }
            setShowModal(false);
            cargarDatos(); // Recargar tablas
        } catch (error) {
            alert("Error al guardar el producto");
        }
    };

    const handleSaveServicio = async (e) => {
      e.preventDefault();
        try {
            await api.post('/api/v1/servicios-costura', servicioEdit);
            setShowModalServicio(false);
            cargarDatos();
            alert("Servicio actualizado correctamente");
        } catch (error) {
            alert("Error al actualizar el servicio");
        }
    };

    const openEdit = (prod) => {
        setProductoEdit(prod);
        setShowModal(true);
    };

    if (loading) return <Container className="text-center mt-5"><Spinner animation="border" variant="success" /></Container>;

    return (
        <Container className="mt-4 mb-5">
            <h2 className="fw-bold mb-4">Dashboard de Administración - Cosarbo</h2>

            <Tabs defaultActiveKey="pedidos" className="mb-4 shadow-sm nav-fill">
                {/* SECCIÓN 1: PEDIDOS */}
                <Tab eventKey="pedidos" title="📦 Pedidos y Despacho">
                    <Card className="border-0 shadow-sm mt-3">
                        <Card.Body>
                            <Table responsive hover>
                                <thead className="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Receptor</th>
                                        <th>Comuna</th>
                                        <th>Fecha</th>
                                        <th>Total</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {pedidos.map(p => (
                                        <tr key={p.idPedido}>
                                            <td>#PED-{p.idPedido}</td>
                                            <td>{p.nombreReceptor}</td>
                                            <td>{p.comuna}</td>
                                            <td>{new Date(p.fechaVenta).toLocaleDateString()}</td>
                                            <td className="fw-bold">${p.totalPagado.toLocaleString('es-CL')}</td>
                                            <td>
                                                <Button size="sm" variant="info" href={`/pagorealizado/${p.carrito.idCarrito}`}>Ver Detalle</Button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Tab>

                {/* SECCIÓN 2: INVENTARIO */}
                <Tab eventKey="inventario" title="🧶 Gestión de Inventario">
                    <div className="d-flex justify-content-end mb-3 mt-3">
                        <Button variant="success" onClick={() => {
                            setProductoEdit({ nombre: '', descripcion: '', precio: '', stock: '', imagen: '' });
                            setShowModal(true);
                        }}>+ Nuevo Producto</Button>
                    </div>
                    <Card className="border-0 shadow-sm">
                        <Card.Body>
                            <Table responsive hover className="align-middle">
                                <thead>
                                    <tr>
                                        <th>Imagen</th>
                                        <th>Nombre</th>
                                        <th>Precio</th>
                                        <th>Stock</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {productos.map(prod => (
                                        <tr key={prod.idProducto}>
                                            <td><img src={prod.imagen} alt={prod.nombre} style={{ width: '40px', borderRadius: '5px' }} /></td>
                                            <td>{prod.nombre}</td>
                                            <td>${prod.precio.toLocaleString('es-CL')}</td>
                                            <td>
                                                <Badge bg={prod.stock === 0 ? "danger" : "info"}>
                                                    {prod.stock} unidades
                                                </Badge>
                                            </td>
                                            <td>
                                                <Button size="sm" variant="outline-warning" className="me-2" onClick={() => openEdit(prod)}>Editar</Button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Tab>

                <Tab eventKey="servicios" title="🧵 Servicios de Costura">
                  <Card className="border-0 shadow-sm mt-3">
                      <Card.Body>
                          <Table responsive hover className="align-middle">
                              <thead className="table-dark">
                                  <tr>
                                      <th>Servicio</th>
                                      <th>Precio Base</th>
                                      <th>Tiempo</th>
                                      <th>Estado Cupo</th>
                                      <th>Acciones</th>
                                  </tr>
                              </thead>
                              <tbody>
                                  {servicios.map(s => (
                                      <tr key={s.idServicio}>
                                          <td className="fw-bold">{s.tipoPrenda}</td>
                                          <td>${s.costo?.toLocaleString('es-CL')}</td>
                                          <td>{s.tiempoEstimado}</td>
                                          <td>
                                              <Badge bg={s.estadoCupo === 'Disponible' ? 'success' : s.estadoCupo === 'Pocos cupos' ? 'warning' : 'danger'}>
                                                  {s.estadoCupo}
                                              </Badge>
                                          </td>
                                          <td>
                                              <Button size="sm" variant="outline-primary" onClick={() => {
                                                  setServicioEdit(s);
                                                  setShowModalServicio(true);
                                              }}>Editar</Button>
                                          </td>
                                      </tr>
                                  ))}
                              </tbody>
                          </Table>
                      </Card.Body>
                  </Card>
              </Tab>
            </Tabs>

            {/* MODAL ÚNICO PARA CREAR/EDITAR */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>{productoEdit.idProducto ? 'Editar' : 'Agregar'} Amigurumi</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSave}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre</Form.Label>
                            <Form.Control name="nombre" value={productoEdit.nombre} onChange={(e) => setProductoEdit({...productoEdit, nombre: e.target.value})} required />
                        </Form.Group>
                        <Row>
                            <Col><Form.Group className="mb-3"><Form.Label>Precio</Form.Label><Form.Control type="number" name="precio" value={productoEdit.precio} onChange={(e) => setProductoEdit({...productoEdit, precio: e.target.value})} required /></Form.Group></Col>
                            <Col><Form.Group className="mb-3"><Form.Label>Stock</Form.Label><Form.Control type="number" name="stock" value={productoEdit.stock} onChange={(e) => setProductoEdit({...productoEdit, stock: e.target.value})} required /></Form.Group></Col>
                        </Row>
                        <Form.Group className="mb-3">
                            <Form.Label>URL Imagen</Form.Label>
                            <Form.Control name="imagen" value={productoEdit.imagen} onChange={(e) => setProductoEdit({...productoEdit, imagen: e.target.value})} />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Cerrar</Button>
                        <Button variant="success" type="submit">Guardar Cambios</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            {/* MODAL PARA EDITAR SERVICIOS DE COSTURA */}
            <Modal show={showModalServicio} onHide={() => setShowModalServicio(false)} centered>
                <Modal.Header closeButton className="bg-success text-white">
                    <Modal.Title>Gestionar Servicio: {servicioEdit.tipoPrenda}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSaveServicio}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Precio Base (CLP)</Form.Label>
                            <Form.Control 
                                type="number" 
                                value={servicioEdit.costo} 
                                onChange={(e) => setServicioEdit({...servicioEdit, costo: e.target.value})}
                                required 
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Tiempo Estimado</Form.Label>
                            <Form.Control 
                                type="text" 
                                value={servicioEdit.tiempoEstimado} 
                                onChange={(e) => setServicioEdit({...servicioEdit, tiempoEstimado: e.target.value})}
                                placeholder="Ej: 24 hrs, 2-3 días..."
                                required 
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Estado del Cupo</Form.Label>
                            <Form.Select 
                                value={servicioEdit.estadoCupo} 
                                onChange={(e) => setServicioEdit({...servicioEdit, estadoCupo: e.target.value})}
                            >
                                <option value="Disponible">Disponible</option>
                                <option value="Pocos cupos">Pocos cupos</option>
                                <option value="Agotado hoy">Agotado hoy</option>
                            </Form.Select>
                            <Form.Text className="text-muted">
                                Esto cambiará el color del Badge en la página de Servicios.
                            </Form.Text>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModalServicio(false)}>
                            Cancelar
                        </Button>
                        <Button variant="success" type="submit">
                            Guardar Cambios
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
}