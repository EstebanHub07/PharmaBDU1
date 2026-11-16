# Registro de incidencias (sesiones 13 y 14)

Cada incidencia se registró antes de corregirla. El ciclo fue: reproducir el problema → identificar o escribir la prueba que falla → corregir la causa → volver a ejecutar las pruebas → commit `fix(...)`.

Estados: **Cerrada** = corregida y verificada con evidencia; **Corregida** = la corrección ya está en el repositorio pero falta la evidencia final; **Abierta** = todavía no se corrige.

| ID | Fecha | Módulo | Descripción y pasos | Esperado | Obtenido | Severidad | Causa raíz | Corrección | Commit | Prueba que evita que vuelva | Estado |
|---|---|---|---|---|---|---|---|---|---|---|---|
| INC-01 | 13/11/2026 | Autenticación (backend) | Después de unir `master` con la rama de la sesión 13 en GitHub, ejecutar `./mvnw package`. | El backend compila. | Error de compilación: el método `cambiarPassword` estaba dos veces en `AuthServiceImpl`. | Crítica | Al resolver el conflicto del merge se aceptaron las dos versiones del método. | Se dejó un solo método (versión de `master` + límite de 72 bytes). | backend `0ea48b1` | La compilación (`./mvnw package`) y `AuthServiceImplTest`. | Cerrada |
| INC-02 | 13/11/2026 | Logs / Clientes | Registrar un cliente con el perfil prod y buscar en `logs/pharma-backend.log` un número de 8 dígitos. | El log no muestra el DNI completo. | El código escribía el DNI completo en el log («Registrando cliente con DNI=…» con los 8 dígitos). | Alta | `ClienteServiceImpl.create` escribía `request.getDni()` directo en el log. | Se enmascara el DNI: solo se ven los dos últimos dígitos. | backend `3cbe3a8` | No está en la suite E2E; se revisa con la búsqueda de secretos en el log (actividad autónoma S13, 0 coincidencias). No se automatizó porque depende del archivo de log del servidor. | Cerrada |
| INC-03 | 13/11/2026 | Ventas | Cargar el volumen de prueba (20 000 ventas) y abrir Ventas. | La SPA pide solo una página de ventas. | `GET /ventas/buscar` devolvía todas las ventas en una sola respuesta y la SPA paginaba en el navegador. | Alta | El endpoint devolvía `List` y no tenía `pagina` ni `tamanio`. | Paginación en el servidor (`PaginacionUtil`, `Page`) y lista de ventas con la página en la URL. | backend `37652b1`, frontend `631bb2b` | E2E-11 (`integracion.spec.ts`) y Postman «200 · Buscar ventas paginadas». | Cerrada |
| INC-04 | 13/11/2026 | Productos y Ventas | En Productos ir a la página 3 y presionar F5. En Ventas, estando en la página 5, cambiar la fecha. | F5 mantiene la página; cambiar un filtro vuelve a la página 1. | F5 volvía a la página 1; el filtro de fecha no reiniciaba la página hasta pulsar «Buscar». | Media | La página estaba guardada en un `signal` y no en la URL. | Página, tamaño, orden y filtros en la URL (`queryParams`). | frontend `7086fd5`, `631bb2b` | `paginacion.spec.ts` (F5 conserva la página) y E2E-11. | Cerrada |
| INC-05 | 13/11/2026 | Ventas (nueva venta) | En Nueva venta buscar «Jarabe para la tos», que tiene stock 0. | El producto no se ofrece. | Aparecía y se podía agregar. | Media | El buscador solo filtraba por `estado`, no por `stock > 0`. | Se filtran los productos con stock mayor a 0. | frontend `9db04e4` | No se automatizó: la prueba depende de tener siempre un producto con stock 0 y la suite va bajando el stock. Se verifica a mano en la matriz (I-02). | Corregida |
| INC-06 | 13/11/2026 | API Productos | `GET /api/v1/productos?pagina=0&tamanio=500`. | 409 con el mensaje de la regla y el `requestId`. | 400 con el cuerpo genérico de Spring, sin `requestId`. | Baja | El controlador validaba con `ResponseStatusException`. | `PaginacionUtil` lanza `ReglaNegocioException` (409). | backend `37652b1` | Postman «409 · Tamaño de página 500» (productos y auditoría). | Cerrada |
| INC-07 | 14/11/2026 | Pruebas E2E | Ejecutar `npx playwright test`. | Las pruebas inician sesión como admin. | Las pruebas con admin fallaban en el login. | Baja | `e2e/apoyo.ts` tenía una contraseña de admin que ya no era la actual. | Se actualizó la contraseña en `apoyo.ts`. | frontend `c10fabe` | Toda la suite: si la contraseña vuelve a estar mal, falla `iniciarSesion`. | Corregida |
| INC-08 | 14/11/2026 | Ventas (nueva venta) | Con 15 000 productos cargados, en Nueva venta buscar «ProductoStock2». | Se encuentra cualquier producto activo. | Solo se busca entre los primeros 100 productos por nombre; los demás no aparecen. | Media | La nueva venta carga una sola página de 100 productos y filtra en el navegador. | Pendiente: búsqueda de productos en el servidor. | — | — | Abierta |

## Resumen

**Por severidad**

| Severidad | Total | Cerradas o corregidas | Abiertas |
|---|---|---|---|
| Crítica | 1 | 1 | 0 |
| Alta | 2 | 2 | 0 |
| Media | 3 | 2 | 1 |
| Baja | 2 | 2 | 0 |
| **Total** | **8** | **7** | **1** |

**Por módulo**

| Módulo | Incidencias |
|---|---|
| Ventas | INC-03, INC-04, INC-05, INC-08 |
| Productos | INC-04, INC-06 |
| Autenticación | INC-01 |
| Logs / Clientes | INC-02 |
| Pruebas E2E | INC-07 |

Queda abierta **INC-08** (media). No impide vender, porque los productos reales de la botica están dentro de los primeros 100, pero con muchos productos habrá que buscar en el servidor. No queda ninguna incidencia crítica ni alta abierta.
