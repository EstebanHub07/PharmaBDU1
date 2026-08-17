package pe.edu.upeu.sysventas.Service.Service;

import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.Model.Categoria;
import pe.edu.upeu.sysventas.Service.Generic.CrudService;

@Service
public interface CategoriaService extends CrudService<Categoria, Long> {
}
