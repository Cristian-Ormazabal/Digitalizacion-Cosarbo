-- ============================================================
--  cosarbo_db - Schema + Datos de prueba
--  Se ejecuta en cada arranque de Spring Boot
-- ============================================================

CREATE DATABASE IF NOT EXISTS cosarbo_db;
USE cosarbo_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS ticket_contacto;
DROP TABLE IF EXISTS item_carrito;
DROP TABLE IF EXISTS carrito;
DROP TABLE IF EXISTS servicio_costura;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuarios;

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

CREATE TABLE usuarios (
    id_usuario  INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rol         VARCHAR(50)  NOT NULL DEFAULT 'cliente'
);

CREATE TABLE producto (
    id_producto INT          PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(150) NOT NULL,
    precio      INT          NOT NULL,
    stock       INT          NOT NULL DEFAULT 0
);

CREATE TABLE servicio_costura (
    id_servicio INT          PRIMARY KEY AUTO_INCREMENT,
    tipo_prenda VARCHAR(100) NOT NULL,
    costo       INT          NOT NULL
);

CREATE TABLE carrito (
    id_carrito    INT         PRIMARY KEY AUTO_INCREMENT,
    total         INT         NOT NULL DEFAULT 0,
    estado_pedido VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    id_usuario    INT         UNIQUE,
    CONSTRAINT cosarbo_fk_carrito_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE item_carrito (
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

CREATE TABLE ticket_contacto (
    id_ticket      INT          PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(100) NOT NULL,
    mensaje        TEXT         NOT NULL,
    id_usuario     INT          NULL,
    CONSTRAINT cosarbo_fk_ticket_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ------------------------------------------------------------
--  DATOS DE PRUEBA
-- ------------------------------------------------------------

INSERT INTO usuarios (nombre, correo, password, rol) VALUES
    ('Admin',        'admin@cosarbo.com',  'admin1234',       'admin'),
    ('María López',  'maria@correo.com',   'hash_maria_456',  'cliente'),
    ('Carlos Pérez', 'carlos@correo.com',  'hash_carlos_789', 'cliente'),
    ('Ana Martínez', 'ana@correo.com',     'hash_ana_321',    'cliente');

INSERT INTO producto (nombre, precio, stock) VALUES
    ('Tela de algodón 1m',      3500,  50),
    ('Hilo de seda (bobina)',     800, 200),
    ('Agujas de costura x10',   1200,  80),
    ('Cremallera 20cm',          600, 150),
    ('Botones decorativos x20', 1500,  60),
    ('Tela de lino 1m',         4200,  30);

INSERT INTO servicio_costura (tipo_prenda, costo) VALUES
    ('Vestido',         15000),
    ('Pantalón',         8000),
    ('Camisa / Blusa',   7500),
    ('Falda',            6000),
    ('Chaqueta',        18000),
    ('Arreglo general',  3500);

INSERT INTO carrito (total, estado_pedido, id_usuario) VALUES
    (12800, 'pendiente',  2),
    (29500, 'confirmado', 3),
    (0,     'pendiente',  4);

INSERT INTO item_carrito (cantidad, sub_total, id_carrito, id_producto, id_servicio) VALUES
    (2,  7000, 1, 1, NULL),
    (1,  1200, 1, 3, NULL),
    (1,   600, 1, 4, NULL),
    (5,  4000, 1, 2, NULL),
    (1,  4200, 2, 6, NULL),
    (2,  3000, 2, 5, NULL),
    (1, 15000, 2, NULL, 1),
    (2,  7000, 2, NULL, 6);

INSERT INTO ticket_contacto (nombre_usuario, mensaje, id_usuario) VALUES
    ('María López',  '¿Tienen telas de seda disponibles para la próxima semana?', 2),
    ('Carlos Pérez', 'Quisiera saber el tiempo estimado para la confección de un vestido.', 3),
    ('Visitante',    'Hola, ¿hacen envíos a regiones?', NULL);