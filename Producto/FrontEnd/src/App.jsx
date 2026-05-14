import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CartProvider } from './Context/CartContext.jsx';

import './Global.css';

import AdminPanel from './Admin/AdminPanel.jsx';

import Home from './Paginas/Home/Home.jsx';

import Catalogo from './Paginas/Catalogo/Catalogo.jsx';
import DetallesProducto from './Paginas/Producto/DetallesProductos.jsx';

import Checkout from './Paginas/Checkout/Checkout.jsx';
import PagoRealizado from './Paginas/Checkout/PagoRealizado.jsx';
import PagoFallido from './Paginas/Checkout/PagoFallido.jsx';

import Login from './Paginas/Autenticacion/Login.jsx';
import Registro from './Paginas/Autenticacion/Registro.jsx';

import Carrito from './Paginas/Carrito/Carrito.jsx';
import Servicios from './Paginas/Servicios/Servicios.jsx';
import Nosotros from './Paginas/Nosotros/Nosotros.jsx';
import MisPedidos from './Paginas/Pedidos/MisPedidos.jsx';

import NavbarShared from './Componentes/Navbar.jsx';
import FooterShared from './Componentes/Footer.jsx';
import ProtectedRoute from './Componentes/ProtectedRoute.jsx';

import NotFound from './Paginas/Error/NotFound.jsx';

function App() {
  return (
    <CartProvider>
      <Router>
        <div className='home-container'>
          <NavbarShared />

          <main style={{ minHeight: '80vh' }}>
            <Routes>
              {/* Rutas Públicas */}
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/registro" element={<Registro />} />
              <Route path="/catalogo" element={<Catalogo />} />
              <Route path="/producto/:id" element={<DetallesProducto />} />
              <Route path="/servicios" element={<Servicios />} />
              <Route path="/nosotros" element={<Nosotros />} />

              {/* Rutas Protegidas - Solo para usuarios autenticados */}
              <Route element={<ProtectedRoute allowedRoles={['CLIENTE', 'admin']} />}>
                <Route path="/carrito" element={<Carrito />} />
                <Route path="/pedidos" element={<MisPedidos />} />
                <Route path="/checkout" element={<Checkout />} />
                <Route path="/pagorealizado/:ordenId" element={<PagoRealizado />} />
                <Route path="/pagofallido" element={<PagoFallido />} />
              </Route>

              {/* Ruta exclusivas de Admin */}
              <Route element={<ProtectedRoute allowedRoles={['admin']} />}>
                <Route path="/admin" element={<AdminPanel />} />
              </Route>

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