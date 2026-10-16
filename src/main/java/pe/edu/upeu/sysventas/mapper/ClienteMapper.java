package pe.edu.upeu.sysventas.mapper;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.dto.ClienteResponseDTO;
import pe.edu.upeu.sysventas.model.Cliente;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getEstado(),
                cliente.getFechaCreacion(),
                cliente.getFechaModificacion()
        );
    }
}