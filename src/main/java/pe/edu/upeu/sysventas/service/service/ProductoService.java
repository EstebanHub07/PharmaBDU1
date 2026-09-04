package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.service.generic.CrudService;

public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO, Long> {
}
