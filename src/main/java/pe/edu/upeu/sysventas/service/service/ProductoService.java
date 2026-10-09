package pe.edu.upeu.sysventas.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.upeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.service.generic.CrudService;

public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO, Long> {
    Page<ProductoResponseDTO> readPage(Pageable pageable);
}
