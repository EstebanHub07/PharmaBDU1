package pe.edu.upeu.sysventas.Service.Generic;

import java.util.Optional;

public interface CrudService <REQ, RES, ID>{
    RES create(REQ t);
    RES update(ID id, REQ t);
    Optional<RES> read(ID id);
    void delete(ID id);
    Iterable<RES> readAll();
}