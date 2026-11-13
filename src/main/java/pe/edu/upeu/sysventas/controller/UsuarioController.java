package pe.edu.upeu.sysventas.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.sysventas.dto.auth.EmpleadoResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.RolResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.AsignarRolesRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.CambiarEstadoRequestDTO;
import pe.edu.upeu.sysventas.service.service.UsuarioService;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(request));
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarEmpleados() {
        return ResponseEntity.ok(usuarioService.listarEmpleados());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RolResponseDTO>> listarRoles() {
        return ResponseEntity.ok(usuarioService.listarRoles());
    }

    @PutMapping("/usuarios/{id}/roles")
    public ResponseEntity<UsuarioResponseDTO> asignarRoles(
            @PathVariable Long id,
            @Valid @RequestBody AsignarRolesRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(usuarioService.asignarRoles(id, request, authentication.getName()));
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, request, authentication.getName()));
    }
}
