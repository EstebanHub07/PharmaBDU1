# Defensa técnica

Respuestas a diez preguntas que me podría hacer el jurado. En cada una indico dónde está el código (`backend` = repositorio PharmaBDU1, `frontend` = pharma-frontend).

## 1. ¿Por qué la paginación se hace en el servidor y no en la SPA? ¿Qué consultas ejecuta Oracle por cada página?

Con más de 15 000 productos y miles de ventas, mandar todo a la SPA haría la respuesta muy pesada y lenta, sobre todo en el local con mala conexión. Por eso el backend devuelve solo una página (por ejemplo 20 productos) y el total. Con 20 productos la respuesta pesa unos 6 KB en vez de cientos de KB.

Por cada página Oracle ejecuta dos consultas: una con `order by … offset ? rows fetch first ? rows only` para traer las filas de esa página y un `select count(*)` para saber el total y calcular cuántas páginas hay.

Además, el backend no deja pedir más de 100 filas ni ordenar por un campo que no esté en la lista permitida; si pasa, responde 409.

- backend: `src/main/java/pe/edu/upeu/sysventas/util/PaginacionUtil.java`, `src/main/java/pe/edu/upeu/sysventas/controller/ProductoController.java`, `src/main/java/pe/edu/upeu/sysventas/repository/VentaRepository.java` (consulta con `countQuery`)
- frontend: `src/app/shared/components/paginador/paginador.ts`, `src/app/features/productos/pages/producto-list/producto-list.ts`

## 2. ¿Cómo garantizas que una venta no deja datos a medias y que su evento de auditoría no aparece si la venta falla?

`registrar` tiene `@Transactional`: guardar la venta, sus detalles y descontar el stock pasa todo junto. Si algo falla (por ejemplo, stock insuficiente), se lanza una excepción y se hace *rollback* de todo.

El evento `VENTA_REGISTRADA` se publica dentro del método, pero `AuditoriaListener` lo guarda con `@TransactionalEventListener(phase = AFTER_COMMIT)`, o sea, solo si la transacción se confirmó. Si hubo rollback, el evento se descarta. El evento se guarda en otra transacción (`REQUIRES_NEW`), para que un error al auditar no deshaga la venta.

- backend: `src/main/java/pe/edu/upeu/sysventas/service/impl/VentaServiceImpl.java` (`registrar`), `src/main/java/pe/edu/upeu/sysventas/auditoria/AuditoriaListener.java`, `src/main/java/pe/edu/upeu/sysventas/service/impl/AuditoriaServiceImpl.java`

## 3. ¿Qué ocurre, paso a paso, cuando el token vence a mitad de una venta?

1. La SPA tiene un temporizador con la hora de vencimiento del token; si llega esa hora, cierra la sesión y manda al login con `?expirada=1` y la página actual en `returnUrl`.
2. Si el token igual llega vencido (o alterado) al backend, el filtro JWT lo rechaza y el backend responde 401.
3. El interceptor de errores de la SPA ve el 401, borra la sesión de `sessionStorage` y va al login con `expirada=1&returnUrl=/ventas/nueva`.
4. El login muestra «Su sesión expiró. Inicie sesión nuevamente».
5. Cuando el vendedor vuelve a entrar, la SPA lo lleva a `returnUrl`, o sea, a la nueva venta. La venta que estaba armando no se registró, porque el backend nunca recibió un token válido.

- backend: `src/main/java/pe/edu/upeu/sysventas/security/JwtAuthenticationFilter.java`, `src/main/java/pe/edu/upeu/sysventas/security/RestAuthenticationEntryPoint.java`
- frontend: `src/app/core/auth/auth-service.ts` (`programarExpiracion`, `cerrarSesion`), `src/app/core/interceptors/error-interceptor.ts`, `src/app/features/auth/pages/login/login.ts`
- Prueba: E2E-14 (`e2e/sesion.spec.ts`)

## 4. ¿Por qué la caché de productos se invalida al registrar una venta? ¿Qué pasaría si no?

La lista de productos se guarda en Redis por 10 minutos para no consultar Oracle cada vez. Pero la venta cambia el stock. Si no se limpiara la caché, durante esos 10 minutos Productos seguiría mostrando el stock de antes y un vendedor podría ofrecer algo que ya no hay. Por eso `registrar` tiene `@CacheEvict(cacheNames = "productos", allEntries = true)`.

- backend: `src/main/java/pe/edu/upeu/sysventas/service/impl/VentaServiceImpl.java`, `src/main/java/pe/edu/upeu/sysventas/service/impl/ProductoServiceImpl.java` (`@Cacheable`), `src/main/java/pe/edu/upeu/sysventas/config/CacheConfig.java`
- Prueba: E2E-07 (`e2e/flujo-venta.spec.ts`) compara el stock antes y después de vender.

## 5. ¿Qué diferencia hay entre un log, una columna de auditoría y un evento de la bitácora?

- **Log:** todo lo que pasa en el servidor, línea por línea, con el `requestId`. Sirve para encontrar errores. Está en la consola o en `logs/pharma-backend.log`.
- **Columna de auditoría:** `creado_por` y `modificado_por` en la misma fila (por ejemplo, en `productos`). Dice quién creó y quién cambió por última vez ese dato, pero no guarda la historia.
- **Evento de la bitácora:** una fila en `auditoria_eventos` por cada operación importante (venta, cambio de precio, login…), con fecha, usuario, detalle y `requestId`. Guarda la historia y la consulta el administrador.

- backend: `src/main/java/pe/edu/upeu/sysventas/config/RequestIdFilter.java`, `src/main/java/pe/edu/upeu/sysventas/config/AuditoriaConfig.java`, `src/main/java/pe/edu/upeu/sysventas/model/Producto.java` (`@CreatedBy`, `@LastModifiedBy`), `src/main/java/pe/edu/upeu/sysventas/model/AuditoriaEvento.java`
- Más detalle en `docs/auditoria.md`.

## 6. ¿Cómo decides si una prueba end-to-end es estable? ¿Qué hiciste con una que fallaba a veces?

Una prueba es estable si pasa siempre, no solo una vez. Por eso ejecuto la suite tres veces seguidas (`--repeat-each=3`) sin reintentos (`retries: 0`) y sin `waitForTimeout`: espero con `expect`, que reintenta solo hasta que la pantalla cambia. Además, cada prueba inicia su propia sesión y no supone valores fijos, por ejemplo lee el stock antes de vender (`stockAntes`).

Cuando una prueba falla, no la reintento: la registro como incidencia, veo la causa en la traza (`trace.zip`) y la corrijo. Me pasó con las pruebas que iniciaban sesión como admin (INC-07): fallaban porque la contraseña en `apoyo.ts` ya no era la actual.

- frontend: `playwright.config.ts`, `e2e/apoyo.ts`, `e2e/flujo-venta.spec.ts`

## 7. ¿Cómo se protege la página de auditoría?

En dos lugares. En la SPA, la ruta `/auditoria` tiene `permisoGuard` con el permiso `USUARIOS_ADMINISTRAR` y la opción del menú solo aparece si el usuario tiene ese permiso. Pero eso no basta, porque alguien podría llamar al API directo. Por eso el backend también protege `/api/v1/auditoria/**` con `hasAuthority(USUARIOS_ADMINISTRAR)`: el vendedor recibe 403.

- frontend: `src/app/app.routes.ts`, `src/app/core/config/menu.ts`, `src/app/core/guards/permiso-guard.ts`
- backend: `src/main/java/pe/edu/upeu/sysventas/config/SecurityConfig.java`
- Pruebas: E2E-13 y Postman «403 · Vendedor consulta la bitácora».

## 8. ¿Qué es el requestId y para qué sirve?

Es un código corto (8 caracteres) que `RequestIdFilter` le pone a cada petición. Va en todas las líneas del log de esa petición, en la cabecera `X-Request-Id`, en las respuestas de error y en los eventos de la bitácora. Si un usuario me dice «me salió un error con código 8df2e633», busco ese código en el log y veo exactamente qué pasó.

- backend: `src/main/java/pe/edu/upeu/sysventas/config/RequestIdFilter.java`, `src/main/java/pe/edu/upeu/sysventas/exception/GlobalExceptionHandler.java`
- frontend: `src/app/core/utils/https-errors.ts` (muestra «Código de seguimiento»)

## 9. ¿Por qué se guardan la página, el tamaño y los filtros en la URL?

Si estuvieran solo en una variable de la SPA, al presionar F5 se perderían y la lista volvería a la página 1 (INC-04). En la URL se conservan al recargar y también se pueden compartir. Cuando cambia un filtro, la página vuelve a 0, porque la página 7 del filtro anterior puede no existir con el nuevo.

- frontend: `src/app/features/productos/pages/producto-list/producto-list.ts`, `src/app/features/ventas/pages/venta-list/venta-list.ts`, `src/app/features/auditoria/pages/auditoria-list/auditoria-list.ts`
- Pruebas: E2E-06 y E2E-11.

## 10. ¿Qué pasa si Redis se cae durante el día?

La SPA sigue funcionando, porque el backend, cuando no puede leer o guardar en Redis, solo escribe una advertencia en el log y consulta Oracle directo (manejador de errores de la caché). Además, el tiempo de espera de Redis es de 500 ms, así que las peticiones no se quedan colgadas. `/actuator/health` pasa a `DOWN` y así nos damos cuenta. Lo probé en la sesión 13 deteniendo el contenedor de Redis.

- backend: `src/main/java/pe/edu/upeu/sysventas/config/CacheConfig.java`, `src/main/resources/application.yaml` (`spring.data.redis.timeout`)
