import React, { useContext, useState } from 'react';
import { Navbar, Nav, Container, NavDropdown, Badge } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { CartContext } from '../context/CartContext';

const NavbarShared = () => {
  const { cart } = useContext(CartContext);
  const navigate = useNavigate();
  
  // SIMULACIÓN DE ESTADO: Cambiar a 'true' para ver cómo queda logueado
  const [isLoggedIn, setIsLoggedIn] = useState(true); 
  const [userName, setUserName] = useState("Brayan");

  const totalItems = cart.reduce((acc, item) => acc + item.quantity, 0);

  const handleLogout = () => {
    setIsLoggedIn(true);
    navigate('/');
  };

  return (
    <Navbar bg="white" expand="lg" className="navbar-cosarbo sticky-top shadow-sm py-3">
      <Container>
        <Navbar.Brand as={Link} to="/" className="fw-bold fs-3 text-success">
          Cosarbo
        </Navbar.Brand>
        
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/servicios" className="nav-link-custom">Servicios de Costura</Nav.Link>
            <Nav.Link as={Link} to="/catalogo" className="nav-link-custom">Amigurumis</Nav.Link>
            <Nav.Link as={Link} to="/nosotros" className="nav-link-custom">Nosotros</Nav.Link>
          </Nav>

          <Nav className="align-items-center">
            {/* CARRITO - Siempre visible */}
            <Nav.Link as={Link} to="/carrito" className="position-relative me-3">
              <i className="bi bi-cart3 fs-4 text-dark"></i>
              {totalItems > 0 && (
                <span 
                  className="position-absolute badge rounded-pill bg-danger"
                  style={{ top: '2px', right: '-5px', fontSize: '0.65rem' }}
                >
                  {totalItems}
                </span>
              )}
            </Nav.Link>

            {/* LÓGICA CONDICIONAL DE USUARIO */}
            {!isLoggedIn ? (
              <>
                <Nav.Link as={Link} to="/login" className="btn-outline-artesanal px-4 rounded-pill me-2">
                  Ingresar
                </Nav.Link>
                <Nav.Link as={Link} to="/registro" className="btn-artesanal px-4 rounded-pill text-white">
                  Registrarse
                </Nav.Link>
              </>
            ) : (
              <NavDropdown 
                title={<span><i className="bi bi-person-circle me-2"></i>{userName}</span>} 
                id="user-dropdown"
                className="fw-bold"
              >
                <NavDropdown.Item as={Link} to="/perfil">Mi Perfil</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/pedidos">Mis Pedidos / Costuras</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout} className="text-danger">
                  <i className="bi bi-box-arrow-right me-2"></i>Cerrar Sesión
                </NavDropdown.Item>
              </NavDropdown>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavbarShared;