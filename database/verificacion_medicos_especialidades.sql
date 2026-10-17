-- Evidencias MySQL: modulo Medicos y Especialidades
USE clinica_san_gabriel;

-- 1) Especialidades registradas
SELECT * FROM especialidades;

-- 2) Medicos registrados
SELECT * FROM medicos;

-- 3) Relacion medico - especialidad (prueba de la clave foranea)
SELECT m.id_medico, m.dni, m.cmp, m.nombres, m.apellidos,
       e.id_especialidad, e.nombre AS especialidad
FROM medicos m
JOIN especialidades e ON e.id_especialidad = m.id_especialidad
ORDER BY e.nombre, m.apellidos;

-- 4) Cantidad de medicos por especialidad
SELECT e.nombre AS especialidad, COUNT(m.id_medico) AS total_medicos
FROM especialidades e
LEFT JOIN medicos m ON m.id_especialidad = e.id_especialidad
GROUP BY e.id_especialidad, e.nombre;

-- 5) Verificar que NO hay DNI ni CMP duplicados (ambas deben devolver 0 filas)
SELECT dni, COUNT(*) AS veces FROM medicos GROUP BY dni HAVING COUNT(*) > 1;
SELECT cmp, COUNT(*) AS veces FROM medicos GROUP BY cmp HAVING COUNT(*) > 1;

-- 6) Restricciones de la tabla (UNIQUE en dni y cmp, FK a especialidades)
SHOW CREATE TABLE medicos;

-- 7) Cada INSERT DEBE FALLAR. Cambia los valores por un DNI/CMP que ya exista.
-- INSERT INTO medicos (dni, nombres, apellidos, cmp, id_especialidad)
--   VALUES ('12345678', 'Prueba', 'Duplicado', 'CMP-NUEVO', 1);        -- Error 1062 (DNI)
-- INSERT INTO medicos (dni, nombres, apellidos, cmp, id_especialidad)
--   VALUES ('87654321', 'Prueba', 'Duplicado', 'CMP-1001', 1);         -- Error 1062 (CMP)
-- INSERT INTO medicos (dni, nombres, apellidos, cmp, id_especialidad)
--   VALUES ('11112222', 'Prueba', 'SinEspecialidad', 'CMP-9999', 9999); -- Error 1452 (FK)
