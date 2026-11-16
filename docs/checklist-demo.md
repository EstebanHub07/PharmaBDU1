# Lista de verificación antes de la demostración

## Servicios
- [ ] Oracle en marcha (puerto 1522) y conexión probada en SQL Developer.
- [ ] Redis en marcha: `docker ps` muestra `deploy-redis-1`.
- [ ] Backend en marcha con el perfil correcto (dev para `ng serve` en 4200, prod para Nginx en 8081).
- [ ] `http://localhost:8080/actuator/health` responde `{"status":"UP"}`.
- [ ] SPA funcionando: `npm start` (4200) o `ng build` + `docker compose up -d` (8081).

## Datos
- [ ] Usuarios y contraseñas probados: admin, vendedor, almacenero.
- [ ] Cliente para la demo: «Ramos» (activo).
- [ ] Producto para la demo: «Paracetamol» con stock suficiente (más de 10).
- [ ] Volumen cargado: más de 5 000 productos (`docs/sql/volumen_pruebas.sql`).
- [ ] Caché limpia antes de empezar: `docker exec deploy-redis-1 redis-cli FLUSHALL`.

## Navegador y herramientas
- [ ] Sesiones cerradas en el navegador.
- [ ] Pestañas preparadas: SPA (login), `/actuator/health`, Postman con la colección cargada.
- [ ] Terminal en `pharma-frontend` lista para `npx playwright test`.
- [ ] Lámina de arquitectura abierta (`docs/arquitectura.png`).

## Plan B
- [ ] Video de respaldo descargado en la laptop (no depender de internet).
- [ ] Capturas de `docs/evidencias/s14-estabilizacion/` a mano.
- [ ] Informe HTML de Playwright guardado (`playwright-report/`).
