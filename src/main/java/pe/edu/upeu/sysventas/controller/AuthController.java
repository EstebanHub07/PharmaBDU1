package pe.edu.upeu.sysventas.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysventas.dto.auth.LoginRequestDTO;
import pe.edu.upeu.sysventas.dto.auth.LoginResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.sysventas.dto.auth.CambioPasswordRequestDTO;
import pe.edu.upeu.sysventas.service.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        return ResponseEntity.ok(authService.usuarioActual(authentication.getName()));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiarPassword(
            Authentication authentication,
            @Valid @RequestBody CambioPasswordRequestDTO request) {
        authService.cambiarPassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
