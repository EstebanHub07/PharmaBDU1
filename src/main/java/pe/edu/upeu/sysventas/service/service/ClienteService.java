package pe.edu.upeu.sysventas.service.service;
import pe.edu.upeu.sysventas.dto.ClienteRequestDTO;
import pe.edu.upeu.sysventas.dto.ClienteResponseDTO;
import pe.edu.upeu.sysventas.service.generic.CrudService;

public interface ClienteService extends CrudService<ClienteRequestDTO, ClienteResponseDTO, Long> {
}