-- ============================================================
--  cosarbo_db - Schema + Datos de prueba
--  Se ejecuta en cada arranque de Spring Boot
-- ============================================================

CREATE DATABASE IF NOT EXISTS cosarbo_db;
USE cosarbo_db;

-- ------------------------------------------------------------
--  DROP en orden inverso para respetar las FK
-- ------------------------------------------------------------
DROP TABLE IF EXISTS TicketContacto;
DROP TABLE IF EXISTS ItemCarrito;
DROP TABLE IF EXISTS Carrito;
DROP TABLE IF EXISTS ServicioCostura;
DROP TABLE IF EXISTS Producto;
DROP TABLE IF EXISTS Usuario;

-- ------------------------------------------------------------
--  TABLAS
-- ------------------------------------------------------------
CREATE TABLE Usuario (
    idUsuario   INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rol         VARCHAR(50)  NOT NULL DEFAULT 'cliente'
);

CREATE TABLE Producto (
    idProducto  INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(150) NOT NULL,
    precio      INT          NOT NULL,
    stock       INT          NOT NULL DEFAULT 0
);

CREATE TABLE ServicioCostura (
    idServicio  INT          PRIMARY KEY AUTO_INCREMENT,
    tipoPrenda  VARCHAR(100) NOT NULL,
    costo       INT          NOT NULL
);

CREATE TABLE Carrito (
    idCarrito     INT         PRIMARY KEY AUTO_INCREMENT,
    idUsuario     INT         NOT NULL,
    total         INT         NOT NULL DEFAULT 0,
    estadoPedido  VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE ItemCarrito (
    idItem      INT  PRIMARY KEY AUTO_INCREMENT,
    idCarrito   INT  NOT NULL,
    idProducto  INT  NULL,
    idServicio  INT  NULL,
    cantidad    INT  NOT NULL DEFAULT 1,
    subTotal    INT  NOT NULL DEFAULT 0,
    CONSTRAINT fk_item_carrito  FOREIGN KEY (idCarrito)  REFERENCES Carrito(idCarrito),
    CONSTRAINT fk_item_producto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto),
    CONSTRAINT fk_item_servicio FOREIGN KEY (idServicio) REFERENCES ServicioCostura(idServicio)
);

CREATE TABLE TicketContacto (
    idTicket      INT          PRIMARY KEY AUTO_INCREMENT,
    idUsuario     INT          NULL,
    nombreUsuario VARCHAR(100) NOT NULL,
    mensaje       TEXT         NOT NULL,
    CONSTRAINT fk_ticket_usuario FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- ------------------------------------------------------------
--  DATOS DE PRUEBA
-- ------------------------------------------------------------

-- Usuario admin para testing (password: admin1234)
INSERT INTO Usuario (nombre, correo, password, rol) VALUES
    ('Admin',        'admin@cosarbo.com',  'admin1234',       'admin'),
    ('María López',  'maria@correo.com',   'hash_maria_456',  'cliente'),
    ('Carlos Pérez', 'carlos@correo.com',  'hash_carlos_789', 'cliente'),
    ('Ana Martínez', 'ana@correo.com',     'hash_ana_321',    'cliente');

INSERT INTO Producto (nombre, precio, stock) VALUES
    ('Tela de algodón 1m',      3500,  50),
    ('Hilo de seda (bobina)',     800, 200),
    ('Agujas de costura x10',   1200,  80),
    ('Cremallera 20cm',          600, 150),
    ('Botones decorativos x20', 1500,  60),
    ('Tela de lino 1m',         4200,  30);

INSERT INTO ServicioCostura (tipoPrenda, costo) VALUES
    ('Vestido',         15000),
    ('Pantalón',         8000),
    ('Camisa / Blusa',   7500),
    ('Falda',            6000),
    ('Chaqueta',        18000),
    ('Arreglo general',  3500);

INSERT INTO Carrito (idUsuario, total, estadoPedido) VALUES
    (2, 12800, 'pendiente'),
    (3, 29500, 'confirmado'),
    (4,     0, 'pendiente');

INSERT INTO ItemCarrito (idCarrito, idProducto, idServicio, cantidad, subTotal) VALUES
    (1, 1, NULL, 2,  7000),
    (1, 3, NULL, 1,  1200),
    (1, 4, NULL, 1,   600),
    (1, 2, NULL, 5,  4000),
    (2, 6, NULL, 1,  4200),
    (2, 5, NULL, 2,  3000),
    (2, NULL, 1, 1, 15000),
    (2, NULL, 6, 2,  7000);

INSERT INTO TicketContacto (idUsuario, nombreUsuario, mensaje) VALUES
    (2, 'María López',  '¿Tienen telas de seda disponibles para la próxima semana?'),
    (3, 'Carlos Pérez', 'Quisiera saber el tiempo estimado para la confección de un vestido.'),
    (NULL, 'Visitante', 'Hola, ¿hacen envíos a regiones?');