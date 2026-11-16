# Matriz de verificacion de consultas y reportes REST

Las respuestas observadas se obtuvieron de los archivos JSON guardados por el cliente HTTP de IntelliJ. La direccion base utilizada fue `http://localhost:8080`.

| N.° | Peticion (metodo y URL completa) | Resultado esperado | Resultado observado | HTTP | Conforme | Evidencia |
|---:|---|---|---|---:|:---:|---|
| 1 | `GET http://localhost:8080/api/v1/ventas/buscar?clienteId=47` | Lista formada solamente por las ventas del cliente 47. | Se obtuvieron 2 ventas del cliente 47, ambas registradas. | 200 | Si | Captura-02-Filtro-Cliente.png |
| 2 | `GET http://localhost:8080/api/v1/ventas/buscar?estado=ANULADA` | Lista formada solamente por ventas anuladas. | Se obtuvieron 7 ventas y todas tienen estado `ANULADA`. | 200 | Si | Captura-03-Filtro-por-estado.png |
| 3 | `GET http://localhost:8080/api/v1/ventas/buscar?desde=2026-09-01` | Ventas realizadas desde el 1 de septiembre de 2026. | Se obtuvieron 10 ventas; todas tienen fecha igual o posterior al 1 de septiembre. | 200 | Si | Captura-04-Filtro-desde-una-fecha.png |
| 4 | `GET http://localhost:8080/api/v1/ventas/buscar?hasta=2026-08-31` | Ventas realizadas hasta el 31 de agosto de 2026. | Se obtuvieron 6 ventas; todas corresponden al mes de agosto. | 200 | Si | Captura-05-Filtro-hasta-una-fecha.png |
| 5 | `GET http://localhost:8080/api/v1/ventas/buscar?clienteId=47&estado=REGISTRADA` | Ventas registradas pertenecientes al cliente 47. | Se obtuvieron 2 ventas; ambas pertenecen al cliente 47 y estan registradas. | 200 | Si | Captura-06-Combinacion-cliente-y-estado.png |
| 6 | `GET http://localhost:8080/api/v1/ventas/buscar?estado=REGISTRADA&desde=2026-08-01&hasta=2026-09-30` | Ventas registradas dentro del rango indicado. | Se obtuvieron 9 ventas; todas estan registradas y dentro del rango. | 200 | Si | Captura-07-Combinacion-estado-y-rango-de-fechas.png |
| 7 | `GET http://localhost:8080/api/v1/ventas/buscar?ordenarPor=fecha&direccion=asc` | Ventas ordenadas desde la fecha mas antigua hasta la mas reciente. | Se obtuvieron 16 ventas; la primera es del 5 de agosto y la ultima del 18 de septiembre. | 200 | Si | Captura-08-Ordenar-por-fecha-ascendente.png |
| 8 | `GET http://localhost:8080/api/v1/ventas/buscar?ordenarPor=fecha&direccion=desc` | Ventas ordenadas desde la fecha mas reciente hasta la mas antigua. | Se obtuvieron 16 ventas; la primera es del 18 de septiembre y la ultima del 5 de agosto. | 200 | Si | Captura-09-Ordenar-por-fecha-descendente.png |
| 9 | `GET http://localhost:8080/api/v1/ventas/buscar?ordenarPor=total&direccion=asc` | Ventas ordenadas desde el total menor hasta el mayor. | Se obtuvieron 16 ventas; los totales comienzan en 7 y terminan en 60. | 200 | Si | Captura-10-Ordenar-por-total-asscendente.png |
| 10 | `GET http://localhost:8080/api/v1/ventas/buscar?ordenarPor=total&direccion=desc` | Ventas ordenadas desde el total mayor hasta el menor. | Se obtuvieron 16 ventas; los totales comienzan en 60 y terminan en 7. | 200 | Si | Captura-11-Ordenar-por-total-descendente.png |
| 11 | `GET http://localhost:8080/api/v1/ventas/buscar?desde=2030-01-01&hasta=2030-12-31` | Arreglo vacio porque no existen ventas en 2030. | Se obtuvo `[]`, sin resultados. | 200 | Si | Captura-12-Caso-sin-resultado.png |
| 12 | `GET http://localhost:8080/api/v1/ventas/buscar?ordenarPor=cliente.dni` | Respuesta de conflicto mediante `ErrorResponseDTO`. | Se obtuvo un error `Conflict` que indica que `cliente.dni` no esta permitido. | 409 | Si | Captura-13-Ordenamiento-no-permitido.png |
| 13 | `GET http://localhost:8080/api/v1/ventas/buscar?desde=01-09-2026` | Respuesta 400 mediante `ErrorResponseDTO` por formato de fecha incorrecto. | Se obtuvo el codigo 400 y un cuerpo `ErrorResponseDTO` con el mensaje que indica que el formato de la fecha es incorrecto. | 400 | Si | Captura-14-Fecha-con-formato-incorrecto.png |
| 14 | `GET http://localhost:8080/api/v1/reportes/ventas-por-categoria?desde=2026-08-01&hasta=2026-09-30` | Lista agrupada por categoria, excluyendo ventas anuladas. | Se obtuvo una lista de 3 categorias con unidades y montos acumulados. | 200 | Si | Captura-15-Reporte-de-ventas-por-categoria.png |
| 15 | `GET http://localhost:8080/api/v1/reportes/productos-mas-vendidos?desde=2026-08-01&hasta=2026-09-30` | Lista de productos ordenada por unidades vendidas, excluyendo ventas anuladas. | Se obtuvo una lista de 6 productos ordenada de mayor a menor cantidad vendida. | 200 | Si | Captura-16-Reporte-de-productos-mas-vendido.png |

## Correccion aplicada

Inicialmente, la peticion 13 devolvia el codigo HTTP 400, pero no conservaba la estructura de `ErrorResponseDTO`. Se agrego el manejo del error de conversion del parametro de fecha en `GlobalExceptionHandler`. Luego de la correccion, la respuesta mantiene el codigo 400 e incluye un mensaje claro sobre el formato de fecha esperado.
