/*
  Script de creación de base de datos para SGP-Rest
  Este script crea el esquema, las tablas, el usuario de la
  aplicación y carga datos de ejemplo.
  Ejecutar en MySQL Workbench conectado como root.
*/
-- Sección de administración (ejecutar una vez en un entorno de desarrollo)
drop database if exists sgprest;
drop user if exists 'sgprest_user'@'%';

-- Creación del esquema
CREATE database sgprest
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación del usuario de la aplicación (mismos datos que application.properties)
create user 'sgprest_user'@'%' identified by 'SgpRest2026!';

-- Asignación de permisos
grant all privileges on sgprest.* to 'sgprest_user'@'%';
flush privileges;

use sgprest;

-- --- Sección de Creación de Tablas ---

-- Tabla de empleados
create table empleado (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  correo VARCHAR(255) NOT NULL,
  puesto VARCHAR(255) NOT NULL,
  area VARCHAR(255) NOT NULL,
  tarifa_hora decimal(38,2) NOT NULL CHECK (tarifa_hora >= 0),
  activo boolean NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  unique (correo))
  ENGINE = InnoDB;

-- Tabla de turnos
create table turno (
  id BIGINT NOT NULL AUTO_INCREMENT,
  empleado_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  hora_inicio TIME(6) NOT NULL,
  hora_fin TIME(6) NOT NULL,
  area VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  foreign key fk_turno_empleado (empleado_id) references empleado(id))
  ENGINE = InnoDB;

-- Tabla de asistencia (marcas de entrada, descanso y salida)
create table asistencia (
  id BIGINT NOT NULL AUTO_INCREMENT,
  empleado_id BIGINT,
  turno_id BIGINT,
  entrada DATETIME(6),
  salida DATETIME(6),
  tardia boolean NOT NULL DEFAULT FALSE,
  minutos_descanso BIGINT NOT NULL DEFAULT 0,
  minutos_trabajados BIGINT NOT NULL DEFAULT 0,
  inicio_descanso DATETIME(6),
  PRIMARY KEY (id),
  foreign key fk_asistencia_empleado (empleado_id) references empleado(id),
  foreign key fk_asistencia_turno (turno_id) references turno(id))
  ENGINE = InnoDB;

-- Tabla de solicitudes (vacaciones, cambio de turno, incapacidad)
create table solicitud (
  id BIGINT NOT NULL AUTO_INCREMENT,
  empleado_id BIGINT,
  tipo VARCHAR(255),
  fecha_inicio DATE,
  fecha_fin DATE,
  motivo VARCHAR(255),
  estado VARCHAR(255),
  PRIMARY KEY (id),
  foreign key fk_solicitud_empleado (empleado_id) references empleado(id))
  ENGINE = InnoDB;

-- --- Sección de Datos de Ejemplo ---

INSERT INTO empleado (nombre, correo, puesto, area, tarifa_hora, activo) VALUES
('Ana Torres', 'ana.torres@lamesa.cr', 'Jefa de cocina', 'Cocina', 3200.00, true),
('Luis Ortega', 'luis.ortega@lamesa.cr', 'Mesero', 'Salón', 2100.00, true),
('Valentina Cruz', 'valentina.cruz@lamesa.cr', 'Bartender', 'Barra', 2300.00, true),
('Diego Mora', 'diego.mora@lamesa.cr', 'Ayudante de cocina', 'Cocina', 1900.00, true),
('Sofía Jiménez', 'sofia.jimenez@lamesa.cr', 'Cajera', 'Caja', 2000.00, true);

-- Turnos de la semana actual (fechas relativas a hoy para que la demo siempre tenga datos)
INSERT INTO turno (empleado_id, fecha, hora_inicio, hora_fin, area) VALUES
(1, CURDATE(), '08:00', '16:00', 'Cocina'),
(2, CURDATE(), '14:00', '22:00', 'Salón'),
(3, CURDATE(), '16:00', '23:00', 'Barra'),
(4, CURDATE(), '08:00', '16:00', 'Cocina'),
(5, CURDATE(), '10:00', '18:00', 'Caja'),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00', '16:00', 'Cocina'),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00', '22:00', 'Salón'),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '16:00', '23:00', 'Barra');

INSERT INTO solicitud (empleado_id, tipo, fecha_inicio, fecha_fin, motivo, estado) VALUES
(1, 'VACACIONES', DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 'Viaje familiar programado', 'PENDIENTE'),
(2, 'CAMBIO_TURNO', DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'Solicito turno de mañana', 'PENDIENTE');
