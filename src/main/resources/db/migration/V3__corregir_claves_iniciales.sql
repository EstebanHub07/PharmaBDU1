-- Corrige únicamente las contraseñas de las tres cuentas de prueba creadas en V2.
-- Contraseña en texto para las pruebas: password
UPDATE usuarios
SET password = '$2a$10$jDPr97MIU43rbCSM1IsXNedl65nIWStofX.fGmex2P6ozi03Avzre'
WHERE username IN ('admin', 'vendedor', 'almacenero');
