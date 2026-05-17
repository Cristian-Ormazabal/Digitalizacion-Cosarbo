import { Navigate, Outlet, useLocation } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
    const location = useLocation();
    
    const token = localStorage.getItem('token'); 
    const isAuthenticated = !!localStorage.getItem('userId') && !!token;
    
    // Se recupera el rol original
    const rawRol = localStorage.getItem('user_role'); 

    const rol = rawRol ? rawRol.replace('ROLE_', '').toUpperCase().trim() : null;

    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    const rolesPermitidosNormalizados = allowedRoles?.map(r => r.toUpperCase());

    if (rolesPermitidosNormalizados && !rolesPermitidosNormalizados.includes(rol)) {
        console.warn("🚨 ACCESO DENEGADO POR ROL INCORRECTO. Expulsando al Home...");
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;