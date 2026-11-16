# Guion de demostración (10 minutos)

Usuarios: **vendedor** (Lucia Ramos) y **admin** (Rosa Elena Mendoza).
Datos: cliente «Ramos», producto «Paracetamol» (con stock).

| Tiempo | Bloque | Qué muestro | Qué digo |
|---|---|---|---|
| 0:00–1:00 | Contexto | Lámina con `docs/arquitectura.png` | La botica tiene tres locales y vende con PharmaSoft. Los usuarios son el vendedor, el almacenero y el administrador. La SPA en Angular habla con PharmaBackend (Spring Boot), que usa Oracle y Redis. |
| 1:00–4:30 | Flujo principal | Login del vendedor → Nueva venta → busco «Ramos» → agrego Paracetamol (2 unidades) → Revisar → Confirmar → comprobante → Productos desde el menú | La venta descuenta el stock y, sin presionar F5, Productos ya muestra el stock nuevo, porque al vender se limpia la caché de productos. |
| 4:30–6:00 | Seguridad | El menú del vendedor no tiene Auditoría; abro `/auditoria` y sale «Acceso denegado»; en Postman: 401 sin token y 403 del vendedor en `/auditoria` | Cada usuario ve solo lo que su rol permite y el backend lo vuelve a comprobar. Si el token vence, la SPA vuelve al login y después a la misma página. |
| 6:00–7:30 | Volumen y rendimiento | Productos con más de 15 000 registros: paginador, página en la URL y F5; `/actuator/health` en el navegador | El servidor devuelve solo 20 productos por página (una consulta para la página y otra para el total). La caché Redis evita consultar Oracle cada vez; Actuator dice si el sistema está bien. |
| 7:30–8:30 | Auditoría | Como admin, página Auditoría filtrada por VENTA_REGISTRADA: aparece la venta que hice; en el log busco su `requestId` | La venta queda con su usuario y su `requestId`, y con ese código la encuentro en el log. |
| 8:30–10:00 | Calidad | `npx playwright test` ejecutándose y `docs/incidencias.md` | La suite tiene 14 pruebas que pasan tres veces seguidas. Registré 8 incidencias y no queda ninguna crítica ni alta abierta. |

## Ensayos cronometrados

| Ensayo | Fecha | Duración | Qué ajusté |
|---|---|---|---|
| 1 | | | |
| 2 | | | |

## Verificación del flujo principal

Ejecuté el flujo principal a mano tres veces, desde una base recién preparada y siguiendo el guion (login del vendedor → cliente → productos → venta → comprobante → stock en Productos).

| Corrida | Fecha | Duración | Desvíos |
|---|---|---|---|
| 1 | | | |
| 2 | | | |
| 3 | | | |

## Video de respaldo

Enlace: _(completar)_
