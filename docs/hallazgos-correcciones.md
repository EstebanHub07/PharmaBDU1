# Hallazgos y correcciones

## 1. La fecha incorrecta no devolvia el formato de error esperado

- **Defecto:** La peticion con `desde=01-09-2026` devolvia HTTP 400, pero el cuerpo no contenia el mensaje completo de `ErrorResponseDTO`.
- **Causa:** `GlobalExceptionHandler` no manejaba `MethodArgumentTypeMismatchException`, que es la excepcion producida al convertir una fecha mal formada.
- **Correccion:** Se agrego un manejador que devuelve HTTP 400 con `timestamp`, `status`, `error`, `message`, `path` y `validationErrors`.

## 2. El navegador no permitia identificar un origen autorizado

- **Defecto:** Ningun origen recibia la cabecera `Access-Control-Allow-Origin`.
- **Causa:** El proyecto no tenia configuracion CORS.
- **Correccion:** Se agrego `CorsConfig`, que permite el acceso desde `http://localhost:4200` y mantiene bloqueado el origen `http://localhost:5500`.

## 3. Los reportes contenian datos duplicados de una prueba anterior

- **Defecto:** Los reportes mostraban categorias y productos antiguos identificados con `AA-S5`, duplicando parte del juego de datos.
- **Causa:** El primer bloque ejecutado en DBeaver confirmo las inserciones antes de que el separador `/` produjera el error `ORA-00900`.
- **Correccion:** Se eliminaron los registros antiguos y se reemplazo el bloque por sentencias SQL simples compatibles con DBeaver.
