package pe.edu.upeu.sysventas.service.service;
import pe.edu.upeu.sysventas.dto.auth.EmpleadoResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.RolResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.AsignarRolesRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.CambiarEstadoRequestDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO request);

    List<UsuarioResponseDTO> listar();

    List<EmpleadoResponseDTO> listarEmpleados();

    List<RolResponseDTO> listarRoles();

    UsuarioResponseDTO asignarRoles(Long id, AsignarRolesRequestDTO request, String usernameSolicitante);

    UsuarioResponseDTO cambiarEstado(Long id, CambiarEstadoRequestDTO request, String usernameSolicitante);
}
