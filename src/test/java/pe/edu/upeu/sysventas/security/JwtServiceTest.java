package pe.edu.upeu.sysventas.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void generaYLeeElUsuarioDelToken() {
        JwtService jwtService = new JwtService(
                "w9Z6dP/dzykNH/uiI3Z/KGjBlP0H4Ji+Tuq4fm2cTNuLD4jWafu1gBL74V2SJEvy", 60);
        User usuario = new User("admin", "password", java.util.List.of());

        String token = jwtService.generarToken(usuario);

        assertThat(jwtService.extraerUsername(token)).isEqualTo("admin");
    }
}
