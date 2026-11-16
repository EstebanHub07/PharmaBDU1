# Auditoría en PharmaSoft

## Qué se audita y por qué

La botica necesita saber **quién hizo qué y cuándo**: quién vendió, quién cambió un precio o quién intentó entrar con una contraseña equivocada. Por eso PharmaBackend guarda la auditoría de dos formas:

1. **Columnas de auditoría** en las tablas `categorias`, `productos`, `clientes` y `ventas`.
2. **Bitácora de eventos** en la tabla `auditoria_eventos`, con una fila por cada operación importante.

La bitácora solo la puede consultar quien tiene el permiso `USUARIOS_ADMINISTRAR` (el administrador), desde la página **Auditoría** de la SPA o con `GET /api/v1/auditoria`.

## Eventos que se registran

| Acción | Cuándo se registra | Qué guarda en el detalle |
|---|---|---|
| `LOGIN_EXITOSO` | Cuando alguien inicia sesión correctamente | Nada (el usuario va en la columna `usuario`) |
| `LOGIN_FALLIDO` | Cuando el usuario o la contraseña son incorrectos | Nada; en `usuario` queda el nombre que se escribió |
| `VENTA_REGISTRADA` | Después de que la venta se guarda | `total=… \| items=…` |
| `PRECIO_MODIFICADO` | Cuando al editar un producto cambia su precio | `precio anterior -> precio nuevo` |
| `PRODUCTO_ELIMINADO` | Cuando se elimina un producto | El nombre del producto |
| `CLIENTE_REGISTRADO` | Cuando se registra un cliente nuevo | Apellidos y nombres (sin el DNI) |
| `CATEGORIA_ELIMINADA` | Cuando se elimina una categoría | El nombre de la categoría |

`CLIENTE_REGISTRADO` y `CATEGORIA_ELIMINADA` son los dos eventos que agregué en la actividad autónoma. Elegí estos porque registrar un cliente y eliminar una categoría son cambios que después alguien puede querer revisar.

Cada evento guarda además la fecha, el usuario, la entidad, el id de la entidad y el `requestId` de la petición.

## Columnas de auditoría, bitácora y logs

| | Columnas de auditoría | Bitácora (`auditoria_eventos`) | Logs |
|---|---|---|---|
| Qué guarda | Quién creó (`creado_por`) y quién modificó por última vez (`modificado_por`) cada fila | Un registro por operación importante | Todo lo que pasa en el servidor, línea por línea |
| Dónde | En la misma tabla (productos, ventas…) | En su propia tabla de Oracle | En la consola o en `logs/pharma-backend.log` |
| Historia | Solo el último cambio | Todos los eventos | Hasta que el archivo rota (14 días) |
| Quién lo usa | Para saber de quién es un dato | El administrador, para revisar operaciones | Soporte técnico, para encontrar errores |

Las columnas las llena Spring Data JPA solo, con el usuario del token (`config/AuditoriaConfig.java` y `@CreatedBy` / `@LastModifiedBy` en las entidades). Si no hay usuario (un proceso interno), se guarda «sistema».

## Por qué se registra después del commit

Los eventos de negocio (venta, precio, etc.) se publican desde el servicio y los guarda `AuditoriaListener` con `@TransactionalEventListener(phase = AFTER_COMMIT)`. Así, si la venta falla y se hace *rollback*, el evento no se guarda y no queda una venta «fantasma» en la bitácora.

El evento se guarda en su propia transacción (`REQUIRES_NEW` en `AuditoriaServiceImpl.registrar`). Si falla la auditoría, se escribe un error en el log, pero la venta, que ya está guardada, no se convierte en error.

## Por qué el login fallido no espera al commit

Cuando el login falla, la operación termina con una excepción y no hay ningún commit que esperar. Si usara el mismo mecanismo, el evento nunca se guardaría. Por eso `AuthServiceImpl.login` llama directamente a `auditoriaService.registrar(...)`, que abre su propia transacción, y recién después vuelve a lanzar el error.

## Qué datos no se guardan nunca

- Contraseñas (ni en texto ni encriptadas).
- El token JWT ni la clave con que se firma.
- El DNI completo de los clientes (en el log solo quedan los dos últimos dígitos y en la bitácora no va).
- Datos de pago o cualquier otro dato personal que no haga falta para saber quién hizo la operación.

## Caso trazado de punta a punta

Elegí la venta **176148**, registrada en la demostración por el vendedor.

| Paso | Dónde | Qué se ve | Captura |
|---|---|---|---|
| 1 | Pestaña Red del navegador | `POST /api/v1/ventas` → 201, cabecera `X-Request-Id: 8df2e633` | `evidencias/s14-estabilizacion/12-I05-red.png` |
| 2 | Tabla `ventas` | Fila 176148 con `creado_por = vendedor` | `evidencias/s14-estabilizacion/trazabilidad-ventas-sql.png` |
| 3 | Bitácora (`GET /api/v1/auditoria?accion=VENTA_REGISTRADA`) | Evento `VENTA_REGISTRADA`, usuario `vendedor`, entidad 176148, `requestId 8df2e633` | `evidencias/s14-estabilizacion/12-I05.png` |
| 4 | Log del backend | Las líneas con `[8df2e633]` (acceso `POST /api/v1/ventas -> 201` y el registro de auditoría) | `evidencias/s14-estabilizacion/trazabilidad-log.png` |

Consulta del paso 2:

```sql
SELECT id, fecha, total, estado, creado_por FROM ventas WHERE id = 176148;
```

Búsqueda del paso 4 (perfil prod):

```powershell
Select-String -Path logs\pharma-backend.log -Pattern "8df2e633"
```
