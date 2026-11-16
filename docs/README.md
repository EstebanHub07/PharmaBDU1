# Evidencias del Proyecto Integrador — PharmaSoft

Alumno: Williams Villaizan · Lenguaje de Programación II · Docente: David Reyna

Repositorios:
- Backend (PharmaBackend): https://github.com/EstebanHub07/PharmaBDU1
- Frontend (PharmaSoft): https://github.com/EstebanHub07/pharma-frontend

Video de la demostración: _(completar)_

## Documentos

| Documento | Contenido |
|---|---|
| [arquitectura.png](arquitectura.png) | Diagrama de componentes y despliegue |
| [auditoria.md](auditoria.md) | Qué se audita, eventos, diferencias con los logs y caso trazado |
| [incidencias.md](incidencias.md) | Registro de incidencias de las sesiones 13 y 14 y resumen |
| [resultados-pruebas.md](resultados-pruebas.md) | Resultados de Playwright (3 corridas) y de Postman |
| [guion-demostracion.md](guion-demostracion.md) | Guion de 10 minutos, ensayos y verificación del flujo principal |
| [checklist-demo.md](checklist-demo.md) | Lista de verificación antes de la demostración |
| [defensa-tecnica.md](defensa-tecnica.md) | Respuestas a diez preguntas del jurado |
| [postman/PharmaBackend.postman_collection.json](postman/PharmaBackend.postman_collection.json) | Colección de Postman (casos 200 a 409) |
| [sql/volumen_pruebas.sql](sql/volumen_pruebas.sql) | Script para cargar volumen de prueba |

## Evidencias por unidad

| Carpeta | Contenido |
|---|---|
| [evidencias/u1-backend](evidencias/u1-backend) | Sesiones 1 a 5 |
| [evidencias/u2-spa-segura](evidencias/u2-spa-segura) | Sesiones 7 a 11 |
| [evidencias/s13-optimizacion](evidencias/s13-optimizacion) | Antes y después de la sesión 13 ([informe práctica](evidencias/s13-optimizacion/Villaizan_LP2_S13_Practica.pdf), [resultados autónoma](evidencias/s13-optimizacion/autonoma)) |
| [evidencias/s14-estabilizacion](evidencias/s14-estabilizacion) | Matriz, auditoría y Playwright ([informe práctica](evidencias/s14-estabilizacion/Villaizan_LP2_S14_Practica.pdf)) |

Evidencias anteriores (sesión 10): [matriz-verificacion.md](matriz-verificacion.md) y [hallazgos-correcciones.md](hallazgos-correcciones.md).

### Sesión 14

| Captura | Qué muestra |
|---|---|
| [02-pagina0.png](evidencias/s14-estabilizacion/02-pagina0.png) | Productos página 0, tamaño 20 (Postman) |
| [03-tamanio100.png](evidencias/s14-estabilizacion/03-tamanio100.png) | Productos con tamaño 100 |
| [04-ultima.png](evidencias/s14-estabilizacion/04-ultima.png) | Última página de productos |
| [05-409.png](evidencias/s14-estabilizacion/05-409.png) | Tamaño 500 → 409 |
| [06-ventas.png](evidencias/s14-estabilizacion/06-ventas.png) | Ventas del 1 al 12 de noviembre, paginadas |
| [07-f5.png](evidencias/s14-estabilizacion/07-f5.png) | SPA: F5 conserva la página |
| [07-productos.png](evidencias/s14-estabilizacion/07-productos.png) | SPA: lista de productos con paginador |
| [08-I01-postman.png](evidencias/s14-estabilizacion/08-I01-postman.png) | I-01: registro de cliente nuevo |
| [08-I01-spa.png](evidencias/s14-estabilizacion/08-I01-spa.png) | I-01: el cliente aparece en Nueva venta |
| [09-I02-sin-stock-vendedor.png](evidencias/s14-estabilizacion/09-I02-sin-stock-vendedor.png) | I-02: producto con stock 0 (vendedor) |
| [09-I02-sin-stock.png](evidencias/s14-estabilizacion/09-I02-sin-stock.png) | I-02: producto con stock 0 (admin) |
| [10-I03-antes.png](evidencias/s14-estabilizacion/10-I03-antes.png) | I-03: stock antes de la venta |
| [10-I03-despues.png](evidencias/s14-estabilizacion/10-I03-despues.png) | I-03: stock después de la venta |
| [11-I04-antes.png](evidencias/s14-estabilizacion/11-I04-antes.png) | I-04: reporte de más vendidos antes de la venta |
| [12-I05-red.png](evidencias/s14-estabilizacion/12-I05-red.png) | I-05: X-Request-Id de la venta |
| [12-I05.png](evidencias/s14-estabilizacion/12-I05.png) | I-05: evento VENTA_REGISTRADA |
| [13-I06-postman.png](evidencias/s14-estabilizacion/13-I06-postman.png) | I-06: evento PRECIO_MODIFICADO |
