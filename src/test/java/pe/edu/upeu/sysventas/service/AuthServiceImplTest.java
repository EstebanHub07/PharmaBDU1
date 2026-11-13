package pe.edu.upeu.sysventas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.sysventas.dto.auth.CambioPasswordRequestDTO;
import pe.edu.upeu.sysventas.exception.ReglaNegocioException;
import pe.edu.upeu.sysventas.mapper.UsuarioMapper;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.repository.UsuarioRepository;
import pe.edu.upeu.sysventas.security.JwtService;
import pe.edu.upeu.sysventas.service.impl.AuthServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private UsuarioMapper usuarioMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AuthServiceImpl authService;

    @Test
    void cambiaLaContrasenaCuandoLaActualEsCorrecta() {
        Usuario usuario = new Usuario();
        usuario.setUsername("vendedor");
        usuario.setPassword("hash-actual");
        CambioPasswordRequestDTO request = request("Vendedor2026*", "NuevaClave2026");
        when(usuarioRepository.findByUsernameIgnoreCase("vendedor")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Vendedor2026*", "hash-actual")).thenReturn(true);
        when(passwordEncoder.matches("NuevaClave2026", "hash-actual")).thenReturn(false);
        when(passwordEncoder.encode("NuevaClave2026")).thenReturn("hash-nuevo");

        authService.cambiarPassword("vendedor", request);

        verify(passwordEncoder).encode("NuevaClave2026");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void rechazaSiLaContrasenaActualNoCoincide() {
        Usuario usuario = new Usuario();
        usuario.setPassword("hash-actual");
        when(usuarioRepository.findByUsernameIgnoreCase("vendedor")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Incorrecta2026", "hash-actual")).thenReturn(false);

        assertThatThrownBy(() -> authService.cambiarPassword("vendedor", request("Incorrecta2026", "NuevaClave2026")))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessage("La contraseña actual no es correcta");
    }

    private CambioPasswordRequestDTO request(String actual, String nueva) {
        CambioPasswordRequestDTO request = new CambioPasswordRequestDTO();
        request.setPasswordActual(actual);
        request.setPasswordNueva(nueva);
        return request;
    }
}
