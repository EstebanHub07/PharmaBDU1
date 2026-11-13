package pe.edu.upeu.sysventas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.sysventas.dto.auth.AsignarRolesRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.CambiarEstadoRequestDTO;
import pe.edu.upeu.sysventas.exception.ReglaNegocioException;
import pe.edu.upeu.sysventas.mapper.UsuarioMapper;
import pe.edu.upeu.sysventas.model.Rol;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.repository.EmpleadoRepository;
import pe.edu.upeu.sysventas.repository.RolRepository;
import pe.edu.upeu.sysventas.repository.UsuarioRepository;
import pe.edu.upeu.sysventas.service.impl.UsuarioServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private EmpleadoRepository empleadoRepository;
    @Mock private RolRepository rolRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UsuarioMapper usuarioMapper;
    @InjectMocks private UsuarioServiceImpl usuarioService;

    @Test
    void reemplazaLosRolesDelUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("supervisor");
        Rol vendedor = rol(2L, "VENDEDOR");
        Rol almacenero = rol(3L, "ALMACENERO");
        AsignarRolesRequestDTO request = new AsignarRolesRequestDTO();
        request.setRolIds(Set.of(2L, 3L));
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findAllById(request.getRolIds())).thenReturn(List.of(vendedor, almacenero));

        usuarioService.asignarRoles(10L, request, "admin");

        assertThat(usuario.getRoles()).containsExactlyInAnyOrder(vendedor, almacenero);
    }

    @Test
    void impideQueAdminSeDesactiveASiMismo() {
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        CambiarEstadoRequestDTO request = new CambiarEstadoRequestDTO();
        request.setEstado(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> usuarioService.cambiarEstado(1L, request, "admin"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessage("Un administrador no puede desactivarse a sí mismo");
    }

    private Rol rol(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        return rol;
    }
}
