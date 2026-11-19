-- ============================================================================
-- Base de datos: gestor_polizas
-- Sistema de Gestión de Pólizas (Fase 3 - Normalizada a 3FN)
-- ============================================================================

DROP DATABASE IF EXISTS gestor_polizas;
CREATE DATABASE gestor_polizas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE gestor_polizas;
SET NAMES utf8mb4;

-- ============================================================================
-- 1. Tablas Catálogo e Independientes
-- ============================================================================

CREATE TABLE usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(20) NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'VENDEDOR', 'DESPACHO') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellidoPaterno VARCHAR(80) NOT NULL,
    apellidoMaterno VARCHAR(80) NULL,
    rfc VARCHAR(13) NOT NULL UNIQUE,
    ine VARCHAR(30) NULL,
    telefono VARCHAR(20) NULL,
    correo VARCHAR(120) NULL,
    direccion VARCHAR(255) NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE aseguradora (
    idAseguradora INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20) NULL
) ENGINE=InnoDB;

CREATE TABLE cobertura (
    idCobertura INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('AMPLIA', 'LIMITADA', 'DANOS_TERCEROS') NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL
) ENGINE=InnoDB;

-- ============================================================================
-- 2. Tablas Dependientes (Nivel 1)
-- ============================================================================

CREATE TABLE vehiculo (
    idVehiculo INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    marca VARCHAR(60) NOT NULL,
    modelo VARCHAR(60) NOT NULL,
    anio INT NOT NULL,
    serie VARCHAR(50) NOT NULL UNIQUE,
    placas VARCHAR(20) NOT NULL UNIQUE,
    tarjetaCirculacion VARCHAR(50) NULL,
    CONSTRAINT fk_vehiculo_cliente
        FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================================================
-- 3. Tablas Transaccionales Principales (Nivel 2)
-- ============================================================================

CREATE TABLE cotizacion (
    idCotizacion INT AUTO_INCREMENT PRIMARY KEY,
    idVehiculo INT NOT NULL,
    idUsuario INT NOT NULL,
    idCobertura INT NOT NULL,
    fechaCotizacion DATE NOT NULL,
    primaEstimada DECIMAL(10, 2) NOT NULL,
    estatus ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT chk_cotizacion_prima CHECK (primaEstimada > 0),
    CONSTRAINT fk_cotizacion_vehiculo
        FOREIGN KEY (idVehiculo) REFERENCES vehiculo(idVehiculo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_cotizacion_usuario
        FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_cotizacion_cobertura
        FOREIGN KEY (idCobertura) REFERENCES cobertura(idCobertura)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE poliza (
    idPoliza INT AUTO_INCREMENT PRIMARY KEY,
    numeroPoliza VARCHAR(50) NOT NULL UNIQUE,
    idVehiculo INT NOT NULL,
    idAseguradora INT NOT NULL,
    idCobertura INT NOT NULL,
    idUsuario INT NOT NULL,
    idCotizacion INT NULL UNIQUE, -- Cardinalidad 1 : 0..1 desde cotizacion
    fechaInicio DATE NOT NULL,
    fechaVencimiento DATE NOT NULL,
    primaTotal DECIMAL(10, 2) NOT NULL,
    estatus ENUM('ACTIVA', 'POR_VENCER', 'VENCIDA', 'CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT chk_poliza_fechas CHECK (fechaInicio < fechaVencimiento),
    CONSTRAINT chk_poliza_prima CHECK (primaTotal > 0),
    CONSTRAINT fk_poliza_vehiculo
        FOREIGN KEY (idVehiculo) REFERENCES vehiculo(idVehiculo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_poliza_aseguradora
        FOREIGN KEY (idAseguradora) REFERENCES aseguradora(idAseguradora)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_poliza_cobertura
        FOREIGN KEY (idCobertura) REFERENCES cobertura(idCobertura)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_poliza_usuario
        FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_poliza_cotizacion
        FOREIGN KEY (idCotizacion) REFERENCES cotizacion(idCotizacion)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================================================
-- 4. Tablas Operativas de Seguimiento (Nivel 3)
-- ============================================================================

CREATE TABLE pago (
    idPago INT AUTO_INCREMENT PRIMARY KEY,
    idPoliza INT NOT NULL,
    fechaPago DATE NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    metodoPago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL,
    numeroRecibo VARCHAR(50) NOT NULL UNIQUE,
    parcialidad INT NOT NULL,
    CONSTRAINT chk_pago_monto CHECK (monto > 0),
    CONSTRAINT chk_pago_parcialidad CHECK (parcialidad > 0),
    CONSTRAINT fk_pago_poliza
        FOREIGN KEY (idPoliza) REFERENCES poliza(idPoliza)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE renovacion (
    idRenovacion INT AUTO_INCREMENT PRIMARY KEY,
    idPoliza INT NOT NULL,
    fechaSolicitud DATE NOT NULL,
    fechaInicioNueva DATE NOT NULL,
    nuevaPrima DECIMAL(10, 2) NOT NULL,
    estado ENUM('SOLICITADA', 'APROBADA', 'RECHAZADA') NOT NULL DEFAULT 'SOLICITADA',
    CONSTRAINT chk_renovacion_prima CHECK (nuevaPrima > 0),
    CONSTRAINT fk_renovacion_poliza
        FOREIGN KEY (idPoliza) REFERENCES poliza(idPoliza)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE cancelacion (
    idCancelacion INT AUTO_INCREMENT PRIMARY KEY,
    idPoliza INT NOT NULL UNIQUE, -- Cardinalidad 1 : 0..1 desde poliza
    idUsuario INT NOT NULL,
    fechaCancelacion DATE NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    estado ENUM('SOLICITADA', 'PROCESADA', 'RECHAZADA') NOT NULL DEFAULT 'SOLICITADA',
    CONSTRAINT fk_cancelacion_poliza
        FOREIGN KEY (idPoliza) REFERENCES poliza(idPoliza)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_cancelacion_usuario
        FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================================================
-- 5. Datos de prueba
-- ============================================================================

INSERT INTO usuario (nombre, correo, telefono, contrasena, rol) VALUES
('Admin General', 'admin@despacho.com', '9841000001', 'admin123', 'ADMINISTRADOR'),
('Laura Méndez', 'laura@despacho.com', '9841000002', 'laura123', 'VENDEDOR'),
('Carlos Ruiz', 'carlos@despacho.com', '9841000003', 'carlos123', 'DESPACHO');

INSERT INTO cliente (nombre, apellidoPaterno, apellidoMaterno, rfc, ine, telefono, correo, direccion) VALUES
('Juan', 'Pérez', 'López', 'PELJ900101AB1', 'INE001', '9842000001', 'juan@correo.com', 'Calle 10, Playa del Carmen'),
('María', 'García', 'Torres', 'GATM850515CD2', 'INE002', '9842000002', 'maria@correo.com', 'Av. Juárez 25, Playa del Carmen'),
('Pedro', 'Sánchez', 'Díaz', 'SADP920320EF3', 'INE003', '9842000003', 'pedro@correo.com', 'Calle 30, Tulum');

INSERT INTO aseguradora (nombre, telefono) VALUES
('GNP Seguros', '8005005000'),
('Qualitas', '8008002880'),
('AXA Seguros', '8009001292');

INSERT INTO cobertura (tipo, descripcion) VALUES
('AMPLIA', 'Daños materiales, robo, responsabilidad civil y gastos médicos'),
('LIMITADA', 'Robo total y responsabilidad civil'),
('DANOS_TERCEROS', 'Solo responsabilidad civil');

INSERT INTO vehiculo (idCliente, marca, modelo, anio, serie, placas, tarjetaCirculacion) VALUES
(1, 'Nissan', 'Versa', 2022, '3N1CN7AD5NL000001', 'ABC-123', 'TC-0001'),
(2, 'Volkswagen', 'Jetta', 2020, '3VWDB7AJ5LM000002', 'DEF-456', 'TC-0002'),
(3, 'Chevrolet', 'Aveo', 2019, '3G1TB5CF1KL000003', 'GHI-789', 'TC-0003'),
(1, 'Toyota', 'Hilux', 2021, 'MR0FA3CD5M0000004', 'JKL-012', 'TC-0004');

INSERT INTO cotizacion (idVehiculo, idUsuario, idCobertura, fechaCotizacion, primaEstimada, estatus) VALUES
(1, 2, 1, '2026-01-05', 8500.00, 'ACEPTADA'),
(2, 2, 2, '2026-02-10', 5200.00, 'ACEPTADA'),
(3, 2, 3, '2026-03-15', 3100.00, 'ACEPTADA'),
(4, 2, 1, '2026-10-01', 12000.00, 'PENDIENTE');

INSERT INTO poliza (numeroPoliza, idVehiculo, idAseguradora, idCobertura, idUsuario, idCotizacion, fechaInicio, fechaVencimiento, primaTotal, estatus) VALUES
('POL-0001', 1, 1, 1, 3, 1, '2026-01-10', '2027-01-10', 8500.00, 'ACTIVA'),
('POL-0002', 2, 2, 2, 3, 2, '2026-02-15', '2027-02-15', 5200.00, 'ACTIVA'),
('POL-0003', 3, 3, 3, 3, 3, '2025-11-20', '2026-11-20', 3100.00, 'CANCELADA');

INSERT INTO pago (idPoliza, fechaPago, monto, metodoPago, numeroRecibo, parcialidad) VALUES
(1, '2026-01-10', 2125.00, 'EFECTIVO', 'REC-0001', 1),
(1, '2026-04-10', 2125.00, 'TRANSFERENCIA', 'REC-0002', 2),
(2, '2026-02-15', 5200.00, 'TARJETA', 'REC-0003', 1),
(3, '2025-11-20', 1550.00, 'EFECTIVO', 'REC-0004', 1);

INSERT INTO renovacion (idPoliza, fechaSolicitud, fechaInicioNueva, nuevaPrima, estado) VALUES
(1, '2026-12-20', '2027-01-10', 8900.00, 'SOLICITADA');

INSERT INTO cancelacion (idPoliza, idUsuario, fechaCancelacion, motivo, estado) VALUES
(3, 3, '2026-06-01', 'El cliente vendió el vehículo', 'PROCESADA');
