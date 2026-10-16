package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.auth.LoginRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.LoginResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);

    UsuarioResponseDTO usuarioActual(String username);
}