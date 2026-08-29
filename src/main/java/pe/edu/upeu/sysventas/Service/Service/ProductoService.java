package pe.edu.upeu.sysventas.Service.Service;

import pe.edu.upeu.sysventas.Dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.Dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.Service.Generic.CrudService;

public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO, Long> {
}
