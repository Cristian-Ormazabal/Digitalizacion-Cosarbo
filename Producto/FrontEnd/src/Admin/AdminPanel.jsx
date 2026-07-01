import React, { useState, useEffect } from 'react';
import api from '../api/apiConfig'; 
import { Container, Table, Button, Badge, Card, Spinner, Row, Col, Modal, Form, Tabs, Tab, Pagination } from 'react-bootstrap';

export default function AdminPanel() {
    const [pedidos, setPedidos] = useState([]);
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [servicios, setServicios] = useState([]);
    const [showModalServicio, setShowModalServicio] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const pedidosPerPage = 15; 

    const indexOfLastPedido = currentPage * pedidosPerPage;
    const indexOfFirstPedido = indexOfLastPedido - pedidosPerPage;
    const currentPedidos = pedidos?.slice(indexOfFirstPedido, indexOfLastPedido) || [];


    const totalPages = Math.ceil((pedidos?.length || 0) / pedidosPerPage);

    const [archivoFoto, setArchivoFoto] = useState(null);

    const [servicioEdit, setServicioEdit] = useState({
        idServicio: null,
        tipoPrenda: '',
        costo: '',
        tiempoEstimado: '',
        estadoCupo: 'Disponible'
    });
    
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
            // Se revienta la caché usando un timestamp único por milisegundo
            const timestamp = new Date().getTime();

            // Se usa 'api.get' para inyectar automáticamente el Bearer Token del Administrador
            const [resPedidos, resProductos, resServicios] = await Promise.all([
                api.get(`/api/v1/pedidos?_=${timestamp}`),
                api.get(`/api/v1/productos?_=${timestamp}`),
                api.get(`/api/v1/servicios-costura?_=${timestamp}`)
            ]);

            setPedidos(Array.isArray(resPedidos.data) ? resPedidos.data : []);
            setProductos(Array.isArray(resProductos.data) ? resProductos.data : []);
            setServicios(Array.isArray(resServicios.data) ? resServicios.data : []);
            setLoading(false);
        } catch (error) {
            console.error("Error al cargar datos:", error);
            setPedidos([]);
            setProductos([]);
            setServicios([]);
            setLoading(false);
        }
    };

    // PROCESAMIENTO UNIFICADO PARA IMÁGENES (CREAR Y EDITAR)
    const handleSave = async (e) => {
        e.preventDefault();

        // Se crea el FormData con los campos actualizados de los inputs
        const formData = new FormData();
        formData.append("nombre", productoEdit.nombre);
        formData.append("descripcion", productoEdit.descripcion || "Sin descripción");
        formData.append("precio", parseFloat(productoEdit.precio));
        formData.append("stock", parseInt(productoEdit.stock));
        
        // Si el administrador seleccionó una foto en este turno, se adjunta
        if (archivoFoto) {
            formData.append("foto", archivoFoto);
        }

        try {
            if (productoEdit.idProducto) {
                // Se apunta al nuevo endpoint de edición enviándole el ID en la URL
                await api.put(`/api/v1/productos/editar/${productoEdit.idProducto}`, formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
                alert("¡Amigurumi actualizado con éxito! 🔄");
            } else {
                await api.post('/api/v1/productos/crear', formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
                alert("¡Amigurumi creado con éxito! 🚀");
            }
            
            // Limpieza de estados y recarga de la grilla
            setShowModal(false);
            setArchivoFoto(null); 
            cargarDatos(); 
        } catch (error) {
            console.error("Error al guardar producto:", error);
            alert("Error al procesar la operación. Revisa la consola.");
        }
    };

    const handleSaveServicio = async (e) => {
      e.preventDefault();
        try {
            // Se usa 'api.post'
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
        setArchivoFoto(null); 
        setShowModal(true);
    };

    const handleEliminar = async (idProducto, nombre) => {
        // Alerta nativa de confirmación para evitar clicks accidentales
        const confirmar = window.confirm(`¿Estás seguro de que deseas eliminar permanentemente el amigurumi "${nombre}"?`);
        
        if (confirmar) {
            try {
                // Se llama de forma segura usando tu instancia que inyecta el Token JWT
                const response = await api.delete(`/api/v1/productos/${idProducto}`);
                
                alert("Producto eliminado correctamente 🗑️");
                cargarDatos(); // Recarga la grilla al instante para que desaparezca de la vista
            } catch (error) {
                console.error("Error al eliminar el producto:", error);
                // Si el backend responde con el error de llave foránea (FK), lo muestra aquí
                alert(error.response?.data || "No se pudo eliminar el producto del inventario.");
            }
        }
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
                                    {/* CAMBIO AQUÍ: Mapeamos 'currentPedidos' en vez de 'pedidos' */}
                                    {currentPedidos.map(p => (
                                        <tr key={p.idPedido}>
                                            <td>#PED-{p.idPedido}</td>
                                            <td>{p.nombreReceptor}</td>
                                            <td>{p.comuna}</td>
                                            <td>{new Date(p.fechaVenta).toLocaleDateString()}</td>
                                            <td className="fw-bold">${p.totalPagado.toLocaleString('es-CL')}</td>
                                            <td>
                                                <Button size="sm" variant="info" href={`/pagorealizado/${p.carrito?.idCarrito}`}>Ver Detalle</Button>
                                            </td>
                                        </tr>
                                    ))}
                                    {currentPedidos.length === 0 && (
                                        <tr>
                                            <td colSpan="6" className="text-center text-muted py-3">No hay pedidos registrados.</td>
                                        </tr>
                                    )}
                                </tbody>
                            </Table>

                            {/* CONTROL DE PAGINACIÓN */}
                            {totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-3">
                                    <Pagination>
                                        <Pagination.First onClick={() => setCurrentPage(1)} disabled={currentPage === 1} />
                                        <Pagination.Prev onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))} disabled={currentPage === 1} />
                                        
                                        {[...Array(totalPages)].map((_, index) => (
                                            <Pagination.Item 
                                                key={index + 1} 
                                                active={index + 1 === currentPage}
                                                onClick={() => setCurrentPage(index + 1)}
                                            >
                                                {index + 1}
                                            </Pagination.Item>
                                        ))}

                                        <Pagination.Next onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))} disabled={currentPage === totalPages} />
                                        <Pagination.Last onClick={() => setCurrentPage(totalPages)} disabled={currentPage === totalPages} />
                                    </Pagination>
                                </div>
                            )}
                        </Card.Body>
                    </Card>
                </Tab>

                {/* SECCIÓN 2: INVENTARIO */}
                <Tab eventKey="inventario" title="🧶 Gestión de Inventario">
                    <div className="d-flex justify-content-end mb-3 mt-3">
                        <Button variant="success" onClick={() => {
                            setProductoEdit({ nombre: '', descripcion: '', precio: '', stock: '', imagen: '' });
                            setArchivoFoto(null);
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
                                            <td>
                                                <img 
                                                    src={prod.imagen || "https://via.placeholder.com/40"} 
                                                    alt={prod.nombre} 
                                                    style={{ width: '40px', height: '40px', borderRadius: '5px', objectFit: 'cover' }} 
                                                />
                                            </td>
                                            <td>{prod.nombre}</td>
                                            <td>${prod.precio.toLocaleString('es-CL')}</td>
                                            <td>
                                                <Badge bg={prod.stock === 0 ? "danger" : "info"}>
                                                    {prod.stock} unidades
                                                </Badge>
                                            </td>
                                            <td>
                                                {/* Botón de Editar existente */}
                                                <Button size="sm" variant="outline-warning" className="me-2" onClick={() => openEdit(prod)}>
                                                    Editar
                                                </Button>
                                                
                                                {/* NUEVO BOTÓN DE ELIMINAR */}
                                                <Button size="sm" variant="outline-danger" onClick={() => handleEliminar(prod.idProducto, prod.nombre)}>
                                                    Eliminar
                                                </Button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Tab>

                {/* SECCIÓN 3: SERVICIOS */}
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

            {/* MODAL ADAPTADO PARA SUBIDA DE ARCHIVOS LOCALES */}
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
                        <Form.Group className="mb-3">
                            <Form.Label>Descripción</Form.Label>
                            <Form.Control as="textarea" rows={2} name="descripcion" value={productoEdit.descripcion} onChange={(e) => setProductoEdit({...productoEdit, descripcion: e.target.value})} />
                        </Form.Group>
                        <Row>
                            <Col><Form.Group className="mb-3"><Form.Label>Precio</Form.Label><Form.Control type="number" name="precio" value={productoEdit.precio} onChange={(e) => setProductoEdit({...productoEdit, precio: e.target.value})} required /></Form.Group></Col>
                            <Col><Form.Group className="mb-3"><Form.Label>Stock</Form.Label><Form.Control type="number" name="stock" value={productoEdit.stock} onChange={(e) => setProductoEdit({...productoEdit, stock: e.target.value})} required /></Form.Group></Col>
                        </Row>
                        
                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold text-success">Seleccionar Foto desde el Equipo</Form.Label>
                            <Form.Control 
                                type="file" 
                                accept="image/*" 
                                onChange={(e) => setArchivoFoto(e.target.files[0])} 
                                required={!productoEdit.idProducto} // <-- SÓLO ES OBLIGATORIO SI EL PRODUCTO ES NUEVO
                            />
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
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModalServicio(false)}>Cancelar</Button>
                        <Button variant="success" type="submit">Guardar Cambios</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
}