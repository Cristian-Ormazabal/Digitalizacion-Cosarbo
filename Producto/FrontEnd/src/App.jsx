import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CartProvider } from './Context/CartContext.jsx';

import './Global.css';

import Home from './Paginas/Home/Home.jsx';

import Catalogo from './Paginas/Catalogo/Catalogo.jsx';
import DetallesProducto from './Paginas/Producto/DetallesProductos.jsx';

import Login from './Paginas/Autenticacion/Login.jsx';
import Registro from './Paginas/Autenticacion/Registro.jsx';

import Carrito from './Paginas/Carrito/Carrito.jsx';
import Servicios from './Paginas/Servicios/Servicios.jsx';
import Nosotros from './Paginas/Nosotros/Nosotros.jsx';
import MisPedidos from './Paginas/Pedidos/MisPedidos.jsx';

import NavbarShared from './Componentes/Navbar.jsx';
import FooterShared from './Componentes/Footer.jsx';

import NotFound from './Paginas/Error/NotFound.jsx';

function App() {
  return (
    <CartProvider>
      <Router>
        <div className='home-container'>
          <NavbarShared />

          <main style={{ minHeight: '80vh' }}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/catalogo" element={<Catalogo />} />
              <Route path="/producto/:id" element={<DetallesProducto />} />
              <Route path="/login" element={<Login />} />
              <Route path="/registro" element={<Registro />} />
              <Route path="/carrito" element={<Carrito />} />
              <Route path="/servicios" element={<Servicios />} />
              <Route path="/nosotros" element={<Nosotros />} />
              <Route path="/pedidos" element={<MisPedidos />} />

              <Route path="*" element={<NotFound />} />
            </Routes>
          </main>

          <FooterShared />
        </div>
      </Router>
    </CartProvider>
  );
}

export default App;