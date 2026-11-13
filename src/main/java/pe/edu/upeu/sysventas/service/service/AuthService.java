package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.auth.LoginRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.LoginResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.CambioPasswordRequestDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);

    UsuarioResponseDTO usuarioActual(String username);

    void cambiarPassword(String username, CambioPasswordRequestDTO request);
}
