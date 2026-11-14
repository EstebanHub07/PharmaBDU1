-- Solo en desarrollo o pruebas. Nunca en producción.
-- Ejecutar después de la migración V4 (paso 4).
-- En PharmaBackend el id de productos sale de la secuencia productos_seq
-- (la entidad Producto usa @SequenceGenerator); ventas usa identity.
insert into productos (id, nombre, precio, stock, estado, categoria_id,
                       fecha_creacion, creado_por)
select productos_seq.nextval,
       'Producto de prueba ' || lpad(level, 5, '0'),
       round(dbms_random.value(1, 200), 2),
       trunc(dbms_random.value(0, 500)),
       true,
       (select min(id) from categorias),
       systimestamp,
       'carga'
from dual connect by level <= 5000;

insert into ventas (fecha, total, estado, cliente_id, creado_por)
select systimestamp - numtodsinterval(dbms_random.value(0, 365), 'DAY'),
       round(dbms_random.value(5, 500), 2),
       'REGISTRADA',
       (select min(id) from clientes),
       'carga'
from dual connect by level <= 20000;
commit;
