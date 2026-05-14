import { Navigate, Outlet, useLocation } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
    const location = useLocation(); // Esto obliga al componente a re-evaluar al navegar
    
    // Recuperamos los datos del localStorage
    const rol = localStorage.getItem('user_role'); 
    const isAuthenticated = !!localStorage.getItem('user_id');

    // DEBUG: Descomenta la línea de abajo para ver en consola qué está leyendo el guardia
    // console.log("Guardia dice:", { isAuthenticated, rol, allowedRoles });

    if (!isAuthenticated) {
        // replace: true evita que el usuario vuelva atrás al login con el botón del navegador
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // Verificamos si el rol actual está dentro de los permitidos
    // Importante: Asegúrate de que en App.jsx pases ['CLIENTE'] en mayúsculas
    if (allowedRoles && !allowedRoles.includes(rol)) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;