-- ============================================================
--  cosarbo_db - Schema + Datos de prueba
--  Se ejecuta en cada arranque de Spring Boot
-- ============================================================

CREATE DATABASE IF NOT EXISTS cosarbo_db;
USE cosarbo_db;

SET FOREIGN_KEY_CHECKS = 0;


SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------
--  TABLAS
--  Con la estrategia por defecto de Hibernate, los campos
--  Java en camelCase se mapean a snake_case en la BD:
--    idUsuario   → id_usuario
--    idCarrito   → id_carrito
--    idProducto  → id_producto
--    idServicio  → id_servicio
--    idTicket    → id_ticket
--    idItem      → id_item
--    tipoPrenda  → tipo_prenda
--    estadoPedido→ estado_pedido
--    subTotal    → sub_total
--    nombreUsuario → nombre_usuario
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario  INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rol         VARCHAR(50)  NOT NULL DEFAULT 'cliente'
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(150) NOT NULL,
    precio      INT          NOT NULL,
    stock       INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS servicio_costura (
    id_servicio INT          PRIMARY KEY AUTO_INCREMENT,
    tipo_prenda VARCHAR(100) NOT NULL,
    descripcion TEXT         NOT NULL,
    costo       INT          NOT NULL,
    tiempo_estimado VARCHAR(50) NOT NULL,
    estado_cupo VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS carrito (
    id_carrito    INT         PRIMARY KEY AUTO_INCREMENT,
    total         INT         NOT NULL DEFAULT 0,
    estado_pedido VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    id_usuario    INT         UNIQUE,
    CONSTRAINT cosarbo_fk_carrito_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE IF NOT EXISTS item_carrito  (
    id_item     INT PRIMARY KEY AUTO_INCREMENT,
    cantidad    INT NOT NULL DEFAULT 1,
    sub_total   INT NOT NULL DEFAULT 0,
    id_carrito  INT NOT NULL,
    id_producto INT NULL,
    id_servicio INT NULL,
    CONSTRAINT cosarbo_fk_item_carrito  FOREIGN KEY (id_carrito)  REFERENCES carrito(id_carrito),
    CONSTRAINT cosarbo_fk_item_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    CONSTRAINT cosarbo_fk_item_servicio FOREIGN KEY (id_servicio) REFERENCES servicio_costura(id_servicio)
);

CREATE TABLE IF NOT EXISTS ticket_contacto  (
    id_ticket      INT          PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(100) NOT NULL,
    mensaje        TEXT         NOT NULL,
    id_usuario     INT          NULL,
    CONSTRAINT cosarbo_fk_ticket_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE IF NOT EXISTS pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL,
    nombre_receptor VARCHAR(150) NOT NULL,
    correo_contacto VARCHAR(100),
    direccion VARCHAR(255) NOT NULL,
    comuna VARCHAR(100) NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_pagado DECIMAL(10,2),
    CONSTRAINT fk_pedido_carrito FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito)
);

CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_servicio INT NOT NULL,
    fecha_reserva DATE NOT NULL,
    estado VARCHAR(20) DEFAULT 'Confirmada',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_servicio) REFERENCES servicio_costura(id_servicio)
);

-- ------------------------------------------------------------
--  DATO ADMIN PRINCIPAL
-- ------------------------------------------------------------

INSERT IGNORE INTO usuarios (nombre, correo, password, rol) 
VALUES ('Admin', 'admin@cosarbo.com', 'admin1234', 'admin');

-- ------------------------------------------------------------
--  DATOS DE PRUEBA PARA SERVICIOS DE COSTURA
-- ------------------------------------------------------------

-- INSERT IGNORE INTO servicio_costura (tipo_prenda, descripcion, costo, tiempo_estimado, estado_cupo) 
-- VALUES 
-- ('Basta Simple Pantalón', 'Ajuste de largo para jeans, pantalones de tela o buzos. Incluye toma de medidas.', 5000, '24 hrs', 'Disponible'),
-- ('Cambio de Cierre Parka', 'Reemplazo de cierre completo en parkas o chaquetas gruesas. No incluye el insumo.', 12000, '48-72 hrs', 'Pocos cupos'),
-- ('Ajuste de Cintura', 'Rebaje de pretina en pantalones o faldas para un calce perfecto.', 8000, '48 hrs', 'Disponible'),
-- ('Bordado de Nombre', 'Bordado personalizado para delantales escolares o pecheras de cocina.', 4500, '24 hrs', 'Agotado hoy'),
-- ('Cambio de Cierre Pantalón', 'Reparación o cambio de cierre en jeans y pantalones de vestir.', 4500, '24 hrs', 'Disponible');