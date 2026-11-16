# Resultados de las pruebas

## 1. Pruebas end-to-end (Playwright)

La suite está en `pharma-frontend/e2e/` y tiene 14 pruebas: las 7 de la práctica y 7 nuevas.

| ID | Archivo | Prueba |
|---|---|---|
| E2E-01 | autenticacion.spec.ts | Una ruta protegida sin sesión lleva al login con `returnUrl` |
| E2E-02 | autenticacion.spec.ts | Credenciales incorrectas muestran el mensaje del backend |
| E2E-03 | autenticacion.spec.ts | Después de iniciar sesión vuelve a la página pedida |
| E2E-04 | permisos.spec.ts | El almacenero no ve Ventas y la URL directa es denegada |
| E2E-05 | permisos.spec.ts | El vendedor ve Productos sin botones de edición |
| E2E-06 | paginacion.spec.ts | Productos se paginan en el servidor y F5 conserva la página |
| E2E-07 | flujo-venta.spec.ts | Flujo principal: el vendedor registra una venta y el stock baja |
| E2E-08 | ventas-validaciones.spec.ts | Venta con cantidad mayor al stock: se avisa y no se registra |
| E2E-09 | ventas-validaciones.spec.ts | Venta sin productos: no se puede confirmar |
| E2E-10 | integracion.spec.ts | Cliente nuevo y venta inmediata (I-01) |
| E2E-11 | integracion.spec.ts | En Ventas, cambiar la fecha en la página 3 vuelve a la página 1 (I-08) |
| E2E-12 | auditoria.spec.ts | La venta registrada aparece en la página de auditoría del admin |
| E2E-13 | auditoria.spec.ts | El vendedor no ve Auditoría y la URL directa es denegada |
| E2E-14 | sesion.spec.ts | Token alterado en sessionStorage: vuelve al login con aviso de sesión expirada |

En la E2E-08 la SPA no deja enviar la venta (avisa «Supera el stock disponible»), así que el 409 del backend no llega a verse en pantalla. Ese 409 se prueba con Postman («409 · Venta con cantidad mayor al stock»).

Ninguna prueba usa `waitForTimeout` y la configuración tiene `retries: 0`.

Comando:

```
npx playwright test --repeat-each=3 --reporter=list,html
```

| Dato | Valor |
|---|---|
| Fecha | _(completar)_ |
| Versión (commit) | _(completar: `git log -1 --format=%h`)_ |
| Pruebas ejecutadas | _(14 × 3 = 42)_ |
| Aprobadas | _(completar)_ |
| Fallidas | _(completar)_ |
| Duración total | _(completar)_ |
| Informe HTML | `evidencias/s14-estabilizacion/playwright-informe.png` |

| Corrida | Aprobadas | Fallidas |
|---|---|---|
| 1 | _/14 | _ |
| 2 | _/14 | _ |
| 3 | _/14 | _ |

## 2. Postman

Colección: `docs/postman/PharmaBackend.postman_collection.json` (se ejecuta con **Run collection**; cada petición comprueba su código de respuesta).

| Módulo | 200/201 | 400 | 401 | 403 | 404 | 409 |
|---|---|---|---|---|---|---|
| Productos | Listar (200), crear (201) | Crear sin nombre | Listar sin token | Vendedor crea producto | Producto 999999 | Tamaño 500 |
| Ventas | Registrar (201), buscar (200) | Venta sin productos | Registrar sin token | Almacenero ve ventas | Venta 999999 | Cantidad mayor al stock |
| Auditoría | Bitácora (200) | Fecha inválida | Sin token | Vendedor consulta | Ruta inexistente | Tamaño 500 |

| Dato | Valor |
|---|---|
| Fecha | _(completar)_ |
| Peticiones | 26 |
| Pruebas aprobadas | _(completar)_ |
| Captura | `evidencias/s14-estabilizacion/postman-runner.png` |
